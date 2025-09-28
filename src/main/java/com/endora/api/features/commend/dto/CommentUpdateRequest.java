package com.endora.api.features.commend.dto;

import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @Size(max = 500, message = "Content cannot exceed 500 characters")
        String content,

        @Size(max = 100, message = "Author cannot exceed 100 characters")
        String author
) {
}
