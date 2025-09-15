package com.endora.api.features.post.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePostDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Body is required")
        String body,

        @NotBlank(message = "Author is required")
        @Size(max = 100, message = "Author name must not exceed 100 characters")
        String author,

        @NotBlank(message = "Author email is required")
        @Email(message = "Invalid email format")
        String authorEmail,

        List<String> tags,
        String category,
        String imageUrl
) {}