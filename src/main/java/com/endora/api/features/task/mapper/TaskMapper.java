package com.endora.api.features.task.mapper;

import com.endora.api.features.task.dto.TaskDto;
import com.endora.api.features.task.model.Task;
import com.endora.api.features.task.model.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskDto.CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : TaskStatus.PENDING);
        return task;
    }

    public TaskDto.TaskResponse toResponse(Task task) {
        return new TaskDto.TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public void updateEntity(Task task, TaskDto.UpdateTaskRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
    }

    public void patchEntity(Task task, TaskDto.PatchTaskRequest request) {
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
    }
}