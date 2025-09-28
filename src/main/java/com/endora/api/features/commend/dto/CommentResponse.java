package com.endora.api.features.commend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String author,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        Boolean isDefault
) {
}
