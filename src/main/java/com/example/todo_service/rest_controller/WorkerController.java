package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.service.TaskService;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestParam Task.Status status) {
        User worker = getCurrentUser();
        Task updatedTask = taskService.updateTaskStatus(taskId, status, worker.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByWorker() {
        User worker = getCurrentUser();
        List<Task> tasks = taskService.getTasksByUser(worker);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getWorkerProfile() {
        User worker = getCurrentUser();
        return ResponseEntity.ok(worker);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateWorkerProfile(@RequestBody User updatedWorker) {
        User worker = userService.updateUser(updatedWorker);
        return ResponseEntity.ok(worker);
    }
}