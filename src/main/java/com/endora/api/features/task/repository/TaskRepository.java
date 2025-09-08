package com.endora.api.features.task.repository;

import com.endora.api.features.task.model.Task;
import com.endora.api.features.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Task> findByTitleOrDescriptionContaining(@Param("keyword") String keyword);

    List<Task> findByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.createdAt >= :startOfDay AND t.createdAt < :endOfDay")
    Long countTasksCreatedToday(@Param("startOfDay") LocalDateTime startOfDay,
                                @Param("endOfDay") LocalDateTime endOfDay);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.createdAt < :dateTime")
    void deleteTasksCreatedBefore(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.createdAt >= :startDateTime AND t.createdAt < :endDateTime")
    Long countTasksCreatedBetween(@Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime);
}