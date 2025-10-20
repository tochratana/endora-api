package com.endora.api.features.user.service.impl;

import com.endora.api.features.user.dto.UserCreateRequest;
import com.endora.api.features.user.dto.UserResponse;
import com.endora.api.features.user.dto.UserUpdateRequest;
import com.endora.api.features.user.model.User;
import com.endora.api.features.user.repository.UserRepository;
import com.endora.api.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final int DAILY_USER_LIMIT = 100;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // Check daily limit for non-default users
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        long todayCreatedCount = userRepository.countNonDefaultUsersCreatedToday(startOfDay);

        if (todayCreatedCount >= DAILY_USER_LIMIT) {
            throw new RuntimeException("Daily user creation limit of " + DAILY_USER_LIMIT + " exceeded");
        }

        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email " + request.email() + " already exists");
        }

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.photo(),
                request.address(),
                false // Not a default user
        );

        User savedUser = userRepository.save(user);
        log.info("Created new user with ID: {}", savedUser.getId());
        return UserResponse.from(savedUser);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponse::from);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Check if email is being updated and if it already exists
        if (request.email() != null && !request.email().equals(user.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.email());
            if (existingUser.isPresent()) {
                throw new RuntimeException("User with email " + request.email() + " already exists");
            }
        }

        // Update only non-null fields
        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }
        if (request.address() != null) {
            user.setAddress(request.address());
        }

        User updatedUser = userRepository.save(user);
        log.info("Updated user with ID: {}", updatedUser.getId());
        return UserResponse.from(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userRepository.deleteById(id);
        log.info("Deleted user with ID: {}", id);
    }

    @Override
    @Transactional
    public void initializeDefaultUsers() {
        long defaultUserCount = userRepository.countDefaultUsers();

        if (defaultUserCount < 15) {
            createDefaultUsers();
            log.info("Initialized default users");
        }
    }

    @Override
    @Transactional
    public void resetToDefaultUsers() {
        userRepository.deleteNonDefaultUsers();

        long defaultUserCount = userRepository.countDefaultUsers();
        if (defaultUserCount < 15) {
            createDefaultUsers();
        }

        log.info("Reset to default users completed");
    }

    private void createDefaultUsers() {
        List<User> defaultUsers = List.of(
                new User("John", "Doe", "john.doe@example.com", "+1-555-0101", "123 Main St, Anytown", "https://dummyjson.com/icon/emilys/128", true),
                new User("Jane", "Smith", "jane.smith@example.com", "+1-555-0102", "456 Oak Ave, Somewhere", "https://dummyjson.com/icon/michaelw/128", true),
                new User("Mike", "Johnson", "mike.johnson@example.com", "+1-555-0103", "789 Pine Rd, Elsewhere", "https://dummyjson.com/icon/sophiab/128", true),
                new User("Sarah", "Williams", "sarah.williams@example.com", "+1-555-0104", "321 Elm St, Nowhere", "https://dummyjson.com/icon/jamesd/128", true),
                new User("David", "Brown", "david.brown@example.com", "+1-555-0105", "654 Maple Dr, Anywhere", "https://dummyjson.com/icon/emmaj/128", true),
                new User("Lisa", "Davis", "lisa.davis@example.com", "+1-555-0106", "987 Cedar Ln, Someplace", "https://dummyjson.com/icon/oliviaw/128", true),
                new User("Robert", "Miller", "robert.miller@example.com", "+1-555-0107", "147 Birch Blvd, Anyplace", "https://dummyjson.com/icon/alexanderj/128", true),
                new User("Emily", "Wilson", "emily.wilson@example.com", "+1-555-0108", "258 Spruce St, Everyplace", "https://dummyjson.com/icon/avab/128", true),
                new User("James", "Moore", "james.moore@example.com", "+1-555-0109", "369 Willow Way, Noplace", "https://dummyjson.com/icon/ethanm/128", true),
                new User("Jessica", "Taylor", "jessica.taylor@example.com", "+1-555-0110", "741 Ash Ave, Somewhereelse", "https://dummyjson.com/icon/isabellad/128", true),
                new User("Daniel", "Anderson", "daniel.anderson@example.com", "+1-555-0111", "852 Poplar Pl, Anywhereelse", "https://dummyjson.com/icon/liamg/128", true),
                new User("Michelle", "Thomas", "michelle.thomas@example.com", "+1-555-0112", "963 Hickory Hts, Elsewhere2", "https://dummyjson.com/icon/miab/128", true),
                new User("Christopher", "Jackson", "chris.jackson@example.com", "+1-555-0113", "159 Walnut Way, Nowhere2", "https://dummyjson.com/icon/noahw/128", true),
                new User("Amanda", "White", "amanda.white@example.com", "+1-555-0114", "267 Chestnut Ct, Anywhere2", "https://dummyjson.com/icon/charlottec/128", true),
                new User("Matthew", "Harris", "matthew.harris@example.com", "+1-555-0115", "375 Sycamore St, Someplace2", "https://dummyjson.com/icon/williamh/128", true)
        );

        userRepository.saveAll(defaultUsers);
    }
}
