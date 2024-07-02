package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.service.TaskService;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping("/create")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        User user = getCurrentUser();
        Task createdTask = taskService.createTask(task, user.getId());
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{taskId}")
    @Secured({"ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<?> getTask(@PathVariable Long taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Secured({"ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/assignWorker/{workerId}")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> assignWorker(@PathVariable Long taskId, @PathVariable Long workerId) {
        Task task = taskService.assignWorker(taskId, workerId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{taskId}/duration")
    @Secured({"ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<?> getTaskDuration(@PathVariable Long taskId) {
        long duration = taskService.getTaskDuration(taskId);
        return ResponseEntity.ok(duration);
    }

    @GetMapping("/user/{userId}")
    @Secured({"ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<Task> tasks = taskService.getTasksByUser(user);
        return ResponseEntity.ok(tasks);
    }
}
