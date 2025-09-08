package com.endora.api.features.task.repository;

import com.endora.api.features.task.model.TaskCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TaskCounterRepository extends JpaRepository<TaskCounter, Long> {

    Optional<TaskCounter> findByDate(LocalDate date);

    @Modifying
    @Query("DELETE FROM TaskCounter tc WHERE tc.date < :date")
    void deleteByDateBefore(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(tc.taskCount, 0) FROM TaskCounter tc WHERE tc.date = :date")
    Integer getTaskCountForDate(@Param("date") LocalDate date);
}