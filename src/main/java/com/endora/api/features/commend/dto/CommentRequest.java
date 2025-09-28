package com.endora.api.features.commend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Content cannot be empty")
        @Size(max = 500, message = "Content cannot exceed 500 characters")
        String content,

        @NotBlank(message = "Author cannot be empty")
        @Size(max = 100, message = "Author cannot exceed 100 characters")
        String author
) {
}
