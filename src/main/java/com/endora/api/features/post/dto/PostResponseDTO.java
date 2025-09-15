package com.endora.api.features.post.dto;

import java.time.LocalDateTime;
import java.util.List;

// Response DTO
public record PostResponseDTO(
        Long id,
        String title,
        String body,
        String author,
        String authorEmail,
        List<String> tags,
        String category,
        String imageUrl,
        Integer views,
        Integer likes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isDefault
) {}
