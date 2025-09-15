package com.endora.api.features.post.service;

import com.endora.api.features.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledService {

    private final PostService postService;

    // Reset to default posts every day at midnight
    @Scheduled(cron = "0 0 0 * * *") // Every day at 00:00:00
    public void dailyReset() {
        try {
            long userPostsBefore = postService.getUserCreatedPostCount();
            postService.resetToDefaultPosts();
            log.info("Daily reset completed. Removed {} user-created posts", userPostsBefore);
        } catch (Exception e) {
            log.error("Error during daily reset", e);
        }
    }

    // Optional: Reset every hour for testing (comment out the daily reset above and uncomment this)
    // @Scheduled(cron = "0 0 * * * *") // Every hour at minute 0
    // public void hourlyReset() {
    //     try {
    //         long userPostsBefore = postService.getUserCreatedPostCount();
    //         postService.resetToDefaultPosts();
    //         log.info("Hourly reset completed. Removed {} user-created posts", userPostsBefore);
    //     } catch (Exception e) {
    //         log.error("Error during hourly reset", e);
    //     }
    // }
}
