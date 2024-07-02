package com.example.todo_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToMany
    @JoinTable(
            name = "task_workers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "worker_id")
    )
    private Set<Worker> workers = new HashSet<>();

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }

    public void setStatus(Status status, User user) {
        if (this.status != Status.DONE && status == Status.DONE && user instanceof Worker) {
            ((Worker) user).incrementCompletedTaskCount();
        }
        this.status = status;
    }
}