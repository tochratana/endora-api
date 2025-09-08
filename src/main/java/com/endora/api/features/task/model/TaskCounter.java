package com.endora.api.features.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "task_counters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private Integer taskCount = 0;

    public TaskCounter(LocalDate date) {
        this.date = date;
        this.taskCount = 0;
    }

    public void incrementCount() {
        this.taskCount++;
    }
}
