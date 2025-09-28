package com.endora.api.features.commend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_default")
    private Boolean isDefault;

    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
        if (isDefault == null) {
            isDefault = false;
        }
    }
}