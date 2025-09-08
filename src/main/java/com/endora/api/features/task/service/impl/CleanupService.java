package com.endora.api.features.task.service.impl;

import com.endora.api.features.task.repository.TaskCounterRepository;
import com.endora.api.features.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CleanupService {

    private final TaskRepository taskRepository;
    private final TaskCounterRepository taskCounterRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    public void performDailyCleanup() {
        log.info("Starting daily cleanup process...");

        try {
            cleanupOldTasks();
            cleanupOldTaskCounters();
            log.info("Daily cleanup process completed successfully");
        } catch (Exception e) {
            log.error("Error during daily cleanup process", e);
        }
    }

    private void cleanupOldTasks() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        log.info("Deleting tasks created before: {}", oneDayAgo);

        long tasksCountBefore = taskRepository.count();
        taskRepository.deleteTasksCreatedBefore(oneDayAgo);
        long tasksCountAfter = taskRepository.count();

        long deletedTasks = tasksCountBefore - tasksCountAfter;
        log.info("Deleted {} old tasks. Remaining tasks: {}", deletedTasks, tasksCountAfter);
    }

    private void cleanupOldTaskCounters() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        log.info("Deleting task counters older than: {}", sevenDaysAgo);

        long countersCountBefore = taskCounterRepository.count();
        taskCounterRepository.deleteByDateBefore(sevenDaysAgo);
        long countersCountAfter = taskCounterRepository.count();

        long deletedCounters = countersCountBefore - countersCountAfter;
        log.info("Deleted {} old task counters. Remaining counters: {}", deletedCounters, countersCountAfter);
    }

    // Manual cleanup method for testing or emergency use
    public void performManualCleanup() {
        log.info("Performing manual cleanup...");
        performDailyCleanup();
    }

    // Get cleanup status
    public CleanupStatus getCleanupStatus() {
        long totalTasks = taskRepository.count();
        long totalCounters = taskCounterRepository.count();
        LocalDateTime nextCleanup = getNextScheduledCleanup();

        return new CleanupStatus(totalTasks, totalCounters, nextCleanup);
    }

    private LocalDateTime getNextScheduledCleanup() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return nextMidnight;
    }

    public record CleanupStatus(
            long totalTasks,
            long totalTaskCounters,
            LocalDateTime nextCleanupTime
    ) {}
}