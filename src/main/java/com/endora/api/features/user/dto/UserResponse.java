package com.endora.api.features.user.dto;


import com.endora.api.features.user.model.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String photo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getPhoto(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
