package com.endora.api.features.task.service;

import com.endora.api.features.task.dto.TaskDto;
import com.endora.api.features.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskDto.TaskResponse createTask(TaskDto.CreateTaskRequest request);

    TaskDto.TaskResponse getTaskById(Long id);

    List<TaskDto.TaskResponse> getAllTasks();

    Page<TaskDto.TaskResponse> getAllTasks(Pageable pageable);

    List<TaskDto.TaskResponse> getTasksByStatus(TaskStatus status);

    List<TaskDto.TaskResponse> searchTasks(String keyword);

    TaskDto.TaskResponse updateTask(Long id, TaskDto.UpdateTaskRequest request);

    TaskDto.TaskResponse patchTask(Long id, TaskDto.PatchTaskRequest request);

    void deleteTask(Long id);

    boolean existsById(Long id);

    // New methods for task limits
    int getCurrentDailyTaskCount();

    int getRemainingTasksForToday();

    boolean canCreateTask();
}
