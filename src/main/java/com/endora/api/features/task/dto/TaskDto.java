package com.endora.api.features.task.dto;

import com.endora.api.features.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TaskDto {

    // Response DTO
    public record TaskResponse(
            Long id,
            String title,
            String description,
            TaskStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    // Create Request DTO
    public record CreateTaskRequest(
            @NotBlank(message = "Title is required")
            String title,

            String description,

            TaskStatus status
    ) {}

    // Update Request DTO
    public record UpdateTaskRequest(
            @NotBlank(message = "Title is required")
            String title,

            String description,

            @NotNull(message = "Status is required")
            TaskStatus status
    ) {}

    // Partial Update Request DTO (for PATCH operations)
    public record PatchTaskRequest(
            String title,
            String description,
            TaskStatus status
    ) {}
}