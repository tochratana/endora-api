package com.endora.api.features.post.repository;

import com.endora.api.features.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find all non-default posts (user created)
    List<Post> findByIsDefaultFalse();

    // Find all default posts
    List<Post> findByIsDefaultTrue();

    // Count non-default posts
    long countByIsDefaultFalse();

    // Delete all non-default posts (for daily reset)
    @Modifying
    @Query("DELETE FROM Post p WHERE p.isDefault = false")
    void deleteAllNonDefaultPosts();

    // Find posts by category
    List<Post> findByCategoryIgnoreCase(String category);

    // Find posts by author
    List<Post> findByAuthorIgnoreCase(String author);

    // Find posts containing tag
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t) = LOWER(:tag)")
    List<Post> findByTagIgnoreCase(String tag);

    // Search posts by title or body
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.body) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchPosts(String query);
}
