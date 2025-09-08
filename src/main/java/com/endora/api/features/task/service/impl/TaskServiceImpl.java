package com.endora.api.features.task.service.impl;

import com.endora.api.features.task.dto.TaskDto;
import com.endora.api.features.task.mapper.TaskMapper;
import com.endora.api.features.task.model.Task;
import com.endora.api.features.task.model.TaskStatus;
import com.endora.api.features.task.repository.TaskRepository;
import com.endora.api.features.task.service.TaskLimitService;
import com.endora.api.features.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskLimitService taskLimitService;

    @Override
    @Transactional
    public TaskDto.TaskResponse createTask(TaskDto.CreateTaskRequest request) {
        log.info("Creating new task with title: {}", request.title());

        // Check daily task limit before creating
        taskLimitService.checkAndIncrementDailyTaskLimit();

        Task task = taskMapper.toEntity(request);
        Task savedTask = taskRepository.save(task);

        log.info("Task created successfully with id: {}. Remaining tasks today: {}",
                savedTask.getId(), taskLimitService.getRemainingTasksForToday());
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskDto.TaskResponse getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        return taskMapper.toResponse(task);
    }

    @Override
    public List<TaskDto.TaskResponse> getAllTasks() {
        log.info("Fetching all tasks");

        List<Task> tasks = taskRepository.findByOrderByCreatedAtDesc();
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TaskDto.TaskResponse> getAllTasks(Pageable pageable) {
        log.info("Fetching all tasks with pagination: {}", pageable);

        Page<Task> taskPage = taskRepository.findAll(pageable);
        return taskPage.map(taskMapper::toResponse);
    }

    @Override
    public List<TaskDto.TaskResponse> getTasksByStatus(TaskStatus status) {
        log.info("Fetching tasks with status: {}", status);

        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto.TaskResponse> searchTasks(String keyword) {
        log.info("Searching tasks with keyword: {}", keyword);

        List<Task> tasks = taskRepository.findByTitleOrDescriptionContaining(keyword);
        return tasks.stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDto.TaskResponse updateTask(Long id, TaskDto.UpdateTaskRequest request) {
        log.info("Updating task with id: {}", id);

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        taskMapper.updateEntity(existingTask, request);
        Task updatedTask = taskRepository.save(existingTask);

        log.info("Task updated successfully with id: {}", updatedTask.getId());
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    @Transactional
    public TaskDto.TaskResponse patchTask(Long id, TaskDto.PatchTaskRequest request) {
        log.info("Partially updating task with id: {}", id);

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        taskMapper.patchEntity(existingTask, request);
        Task patchedTask = taskRepository.save(existingTask);

        log.info("Task partially updated successfully with id: {}", patchedTask.getId());
        return taskMapper.toResponse(patchedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);

        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    @Override
    public int getCurrentDailyTaskCount() {
        return taskLimitService.getCurrentDailyTaskCount();
    }

    @Override
    public int getRemainingTasksForToday() {
        return taskLimitService.getRemainingTasksForToday();
    }

    @Override
    public boolean canCreateTask() {
        return taskLimitService.canCreateTask();
    }
}