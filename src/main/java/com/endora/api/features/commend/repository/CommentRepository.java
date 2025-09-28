package com.endora.api.features.commend.repository;

import com.endora.api.features.commend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt >= :startOfDay")
    long countCommentsCreatedToday(LocalDateTime startOfDay);

    @Query("SELECT c FROM Comment c WHERE c.isDefault = true")
    List<Comment> findDefaultComments();

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.isDefault = false")
    void deleteNonDefaultComments();

    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findAllOrderByCreatedAtDesc();
}