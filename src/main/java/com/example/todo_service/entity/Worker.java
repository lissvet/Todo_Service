package com.example.todo_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_worker")
public class Worker extends User {

    @ManyToMany(mappedBy = "workers", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> workingTasks = new HashSet<>();

    private Integer completedTaskCount = 0;

    public void incrementCompletedTaskCount() {
        this.completedTaskCount += 1;
    }
}