package com.example.todo_service.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "tb_manager")
@Data
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}