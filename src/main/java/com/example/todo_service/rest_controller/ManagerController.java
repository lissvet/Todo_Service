package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.service.TaskService;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/manager")
@Secured("ROLE_MANAGER")
public class ManagerController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/tasks")
    public Task createTask(Task task, Long managerId) {
        User manager = userService.findById(managerId);
        if (!manager.getRoles().contains(User.Role.ROLE_MANAGER)) {
            throw new IllegalArgumentException("User is not a manager");
        }
        task.setManager(manager);
        task.setStartTime(LocalDateTime.now());
        return taskService.createTask(task, managerId);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/tasks/{taskId}/assign-worker")
    public Task assignWorker(@PathVariable Long taskId, @PathVariable Long workerId) {
        User worker = userService.findById(workerId);
        if (!worker.getRoles().contains(User.Role.ROLE_WORKER)) {
            throw new IllegalArgumentException("User is not a worker");
        }
        return taskService.assignWorker(taskId, workerId);
    }
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTaskDetails(@PathVariable Long taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getManagedTasks(@RequestParam Long managerId) {
        List<Task> tasks = taskService.getTasksByManagerId(managerId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/profile/{managerId}")
    public ResponseEntity<User> getManagerProfile(@PathVariable Long managerId) {
        User manager = userService.findById(managerId);
        return ResponseEntity.ok(manager);
    }

    @PutMapping("/profile/{managerId}")
    public ResponseEntity<User> updateManagerProfile(@PathVariable Long managerId, @RequestBody User updatedManager) {
        User manager = userService.updateUser(updatedManager);
        return ResponseEntity.ok(manager);
    }

    @GetMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task.Status> getTaskStatus(@PathVariable Long taskId) {
        Task.Status status = taskService.getTaskStatus(taskId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/tasks/{taskId}/performance")
    public ResponseEntity<Duration> analyzeTaskPerformance(@PathVariable Long taskId) {
        Duration performance = taskService.analyzeTaskPerformance(taskId);
        return ResponseEntity.ok(performance);
    }

}
