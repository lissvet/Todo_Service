package com.example.todo_service.repository;

import com.example.todo_service.entity.User;
import com.example.todo_service.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUsername(String username);
}
