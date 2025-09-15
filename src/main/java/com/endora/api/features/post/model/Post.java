package com.endora.api.features.post.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String authorEmail;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    private String category;
    private String imageUrl;

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false)
    private Integer likes = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDefault = false;

    public Post(String title, String body, String author, String authorEmail, List<String> tags,
                String category, String imageUrl, Integer views, Integer likes, boolean isDefault) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.authorEmail = authorEmail;
        this.tags = tags;
        this.category = category;
        this.imageUrl = imageUrl;
        this.views = views;
        this.likes = likes;
        this.isDefault = isDefault;
    }
}
