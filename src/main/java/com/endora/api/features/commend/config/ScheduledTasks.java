package com.endora.api.features.commend.config;

import com.endora.api.features.commend.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final CommentService commentService;

    // Reset comments every day at midnight (00:00:00)
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyCommentsAtMidnight() {
        log.info("Starting daily comment reset at midnight...");
        try {
            commentService.resetDailyComments();
            log.info("Daily comment reset completed successfully");
        } catch (Exception e) {
            log.error("Failed to reset daily comments: {}", e.getMessage(), e);
        }
    }

    // Optional: Log comment count every hour for monitoring
    @Scheduled(cron = "0 0 * * * *")
    public void logHourlyCommentCount() {
        try {
            long todayCount = commentService.getTodayCommentCount();
            log.info("Current daily comment count: {}/100", todayCount);
        } catch (Exception e) {
            log.error("Failed to get today's comment count: {}", e.getMessage());
        }
    }
}