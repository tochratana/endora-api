package com.endora.api.features.task.service;

import com.endora.api.features.task.model.TaskCounter;
import com.endora.api.features.task.repository.TaskCounterRepository;
import com.endora.api.features.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional

public class TaskLimitService {

    private static final int MAX_TASKS_PER_DAY = 100;

    private final TaskCounterRepository taskCounterRepository;
    private final TaskRepository taskRepository;

    public void checkAndIncrementDailyTaskLimit() {
        LocalDate today = LocalDate.now();

        TaskCounter counter = taskCounterRepository.findByDate(today)
                .orElse(new TaskCounter(today));

        // Double check with actual task count from database
        Long actualTaskCount = getCurrentTaskCountFromDatabase();
        if (actualTaskCount >= MAX_TASKS_PER_DAY) {
            log.warn("Daily task limit exceeded for date: {}. Actual count from DB: {}",
                    today, actualTaskCount);
            throw new RuntimeException(
                    String.format("Daily task limit of %d tasks exceeded. Current count: %d",
                            MAX_TASKS_PER_DAY, actualTaskCount));
        }

        if (counter.getTaskCount() >= MAX_TASKS_PER_DAY) {
            log.warn("Daily task limit exceeded for date: {}. Counter count: {}",
                    today, counter.getTaskCount());
            throw new RuntimeException(
                    String.format("Daily task limit of %d tasks exceeded. Current count: %d",
                            MAX_TASKS_PER_DAY, counter.getTaskCount()));
        }

        counter.incrementCount();
        taskCounterRepository.save(counter);

        log.info("Task count incremented for date: {}. New count: {}", today, counter.getTaskCount());
    }

    private Long getCurrentTaskCountFromDatabase() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return taskRepository.countTasksCreatedToday(startOfDay, endOfDay);
    }

    public int getCurrentDailyTaskCount() {
        LocalDate today = LocalDate.now();
        TaskCounter counter = taskCounterRepository.findByDate(today).orElse(null);

        if (counter == null) {
            // If no counter exists, check actual database count
            Long actualCount = getCurrentTaskCountFromDatabase();
            return actualCount.intValue();
        }

        return counter.getTaskCount();
    }

    public int getRemainingTasksForToday() {
        return MAX_TASKS_PER_DAY - getCurrentDailyTaskCount();
    }

    public boolean canCreateTask() {
        return getCurrentDailyTaskCount() < MAX_TASKS_PER_DAY;
    }
}