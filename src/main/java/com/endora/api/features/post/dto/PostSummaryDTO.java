package com.endora.api.features.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummaryDTO(
        Long id,
        String title,
        String author,
        String category,
        List<String> tags,
        Integer views,
        Integer likes,
        LocalDateTime createdAt,
        boolean isDefault
) {}
