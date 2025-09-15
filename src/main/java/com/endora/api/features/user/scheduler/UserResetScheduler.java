package com.endora.api.features.user.scheduler;


import com.endora.api.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserResetScheduler {

    private final UserService userService;

    // Reset users daily at midnight (0 0 0 * * ?)
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetUsersDaily() {
        log.info("Starting daily user reset...");
        userService.resetToDefaultUsers();
        log.info("Daily user reset completed");
    }
}
