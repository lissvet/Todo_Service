package com.example.todo_service.repository;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByWorkersId(Long workerId);

    List<Task> findByWorkersContains(User user);
}
