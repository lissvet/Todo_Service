package com.example.todo_service.service;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.entity.Worker;
import com.example.todo_service.repository.ManagerRepository;
import com.example.todo_service.repository.TaskRepository;
import com.example.todo_service.repository.UserRepository;
import com.example.todo_service.custom_exception.ResourceNotFoundException;
import com.example.todo_service.repository.WorkerRepository;
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
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ManagerRepository managerRepository;

    public Task createTask(Task task, Long managerId) {
        return managerRepository.findById(managerId).map(manager -> {
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
            task.setStatus(taskDetails.getStatus());
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
            Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
            if (worker.getRoles().contains(User.Role.ROLE_WORKER)) {
                task.getWorkers().add(worker);
            } else {
                throw new IllegalArgumentException("User is not a worker");
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public long getTaskDuration(Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            LocalDateTime endTime = task.getEndTime() != null ? task.getEndTime() : LocalDateTime.now();
            Duration duration = Duration.between(task.getStartTime(), endTime);
            return duration.toMinutes();
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByWorkersContains(user);
    }

    public Task updateTaskStatus(Long taskId, Task.Status status, Long userId) {
        return taskRepository.findById(taskId).map(task -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setStatus(status, user);
            if (status == Task.Status.DONE) {
                task.setEndTime(LocalDateTime.now());
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public List<Task> getTasksByManagerId(Long managerId) {
        User manager = userRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        return taskRepository.findByManager(manager);
    }

    public Duration analyzeTaskPerformance(Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getEndTime() != null ? task.getEndTime() : LocalDateTime.now();
            return Duration.between(startTime, endTime);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public Task.Status getTaskStatus(Long taskId) {
        return taskRepository.findById(taskId)
                .map(Task::getStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }
}
