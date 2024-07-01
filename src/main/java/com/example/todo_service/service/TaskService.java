package com.example.todo_service.service;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.repository.TaskRepository;
import com.example.todo_service.repository.UserRepository;
import com.example.todo_service.сustom_exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(Task task, Long managerId) {
        return userRepository.findById(managerId).map(manager -> {
            if (!manager.getRoles().contains(User.Role.ROLE_MANAGER)) {
                throw new IllegalArgumentException("User is not a manager");
            }
            task.setManager(manager);
            task.setStartTime(LocalDateTime.now());
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.getCompleted());
            if (taskDetails.getStartTime() != null) {
                task.setStartTime(taskDetails.getStartTime());
            }
            if (taskDetails.getEndTime() != null) {
                task.setEndTime(taskDetails.getEndTime());
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task assignWorker(Long taskId, Long workerId) {
        return taskRepository.findById(taskId).map(task -> {
            userRepository.findById(workerId).ifPresent(worker -> {
                if (worker.getRoles().contains(User.Role.ROLE_WORKER)) {
                    task.getWorkers().add(worker);
                }
            });
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public Duration getTaskDuration(Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            LocalDateTime endTime = task.getEndTime() != null ? task.getEndTime() : LocalDateTime.now();
            return Duration.between(task.getStartTime(), endTime);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByWorkersContains(user);
    }
}

