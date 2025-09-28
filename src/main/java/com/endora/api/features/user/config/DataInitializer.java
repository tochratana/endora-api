package com.endora.api.features.user.config;

import com.endora.api.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component("userDataInitializer")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        log.info("Initializing default users...");
        userService.initializeDefaultUsers();
    }
}
