package com.endora.api.features.post.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdatePostDTO(
        @NotNull(message = "ID is required")
        Long id,

        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String body,

        @Size(max = 100, message = "Author name must not exceed 100 characters")
        String author,

        @Email(message = "Invalid email format")
        String authorEmail,

        List<String> tags,
        String category,
        String imageUrl
) {}
