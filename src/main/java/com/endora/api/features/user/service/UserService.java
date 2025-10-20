package com.endora.api.features.user.service;



import com.endora.api.features.user.dto.UserCreateRequest;
import com.endora.api.features.user.dto.UserResponse;
import com.endora.api.features.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void initializeDefaultUsers();
    void resetToDefaultUsers();
}