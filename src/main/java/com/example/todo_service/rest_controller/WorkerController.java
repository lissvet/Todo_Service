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

@RestController
@RequestMapping("/worker")
@Secured("ROLE_WORKER")
public class WorkerController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestParam Task.Status status, @RequestParam Long userId) {
        Task updatedTask = taskService.updateTaskStatus(taskId, status, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/{taskId}/duration")
    public ResponseEntity<Duration> getTaskDuration(@PathVariable Long taskId) {
        Duration duration = taskService.getTaskDuration(taskId);
        return ResponseEntity.ok(duration);
    }

    @GetMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task.Status> getTaskStatus(@PathVariable Long taskId) {
        Task.Status status = taskService.getTaskStatus(taskId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getWorkerProfile(@RequestParam Long userId) {
        User worker = userService.findById(userId);
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateWorkerProfile(@RequestBody User updatedWorker) {
        User worker = userService.updateUser(updatedWorker);
        return ResponseEntity.ok(worker);
    }
}