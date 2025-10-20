package com.endora.api.features.task.controller;

import com.endora.api.features.task.dto.TaskDto;
import com.endora.api.features.task.model.TaskStatus;
import com.endora.api.features.task.service.TaskService;
import com.endora.api.features.task.service.impl.CleanupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;
    private final CleanupService cleanupService;

    @PostMapping
    public ResponseEntity<TaskDto.TaskResponse> createTask(@Valid @RequestBody TaskDto.CreateTaskRequest request) {
        TaskDto.TaskResponse response = taskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto.TaskResponse> getTask(@PathVariable Long id) {
        TaskDto.TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto.TaskResponse>> getAllTasks(
//            @RequestParam(required = false) TaskStatus status,
//            @RequestParam(required = false) String search) {
//
//        List<TaskDto.TaskResponse> responses;
//
//        if (status != null) {
//            responses = taskService.getTasksByStatus(status);
//        } else if (search != null && !search.trim().isEmpty()) {
//            responses = taskService.searchTasks(search);
//        } else {
//            responses = taskService.getAllTasks();
//        }
//
//        return ResponseEntity.ok(responses);
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskDto.TaskResponse> responses = taskService.getAllTasks(pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TaskDto.TaskResponse>> getAllTasksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskDto.TaskResponse> responses = taskService.getAllTasks(pageable);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto.TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDto.UpdateTaskRequest request) {

        TaskDto.TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto.TaskResponse> patchTask(
            @PathVariable Long id,
            @RequestBody TaskDto.PatchTaskRequest request) {

        TaskDto.TaskResponse response = taskService.patchTask(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> taskExists(@PathVariable Long id) {
        boolean exists = taskService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // New endpoints for task limits and cleanup
    @GetMapping("/limits")
    public ResponseEntity<Map<String, Object>> getTaskLimits() {
        int currentCount = taskService.getCurrentDailyTaskCount();
        int remaining = taskService.getRemainingTasksForToday();
        boolean canCreate = taskService.canCreateTask();

        Map<String, Object> limits = Map.of(
                "currentDailyTaskCount", currentCount,
                "remainingTasksToday", remaining,
                "canCreateTask", canCreate,
                "maxTasksPerDay", 100
        );

        return ResponseEntity.ok(limits);
    }

    @PostMapping("/cleanup/manual")
    public ResponseEntity<Map<String, String>> performManualCleanup() {
        cleanupService.performManualCleanup();
        return ResponseEntity.ok(Map.of("message", "Manual cleanup completed successfully"));
    }

    @GetMapping("/cleanup/status")
    public ResponseEntity<CleanupService.CleanupStatus> getCleanupStatus() {
        CleanupService.CleanupStatus status = cleanupService.getCleanupStatus();
        return ResponseEntity.ok(status);
    }
}