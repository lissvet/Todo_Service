package com.example.todo_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_manager")
public class Manager extends User {

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private Set<Task> managedTasks = new HashSet<>();
}