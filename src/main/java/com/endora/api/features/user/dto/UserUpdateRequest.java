package com.endora.api.features.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @Email(message = "Invalid email format")
        String email,

        @Size(max = 15, message = "Phone number must not exceed 15 characters")
        String phone,

        String photo,

        @Size(max = 200, message = "Address must not exceed 200 characters")
        String address
) {}

