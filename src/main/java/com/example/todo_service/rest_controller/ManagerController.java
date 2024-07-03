package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.Task;
import com.example.todo_service.entity.User;
import com.example.todo_service.service.TaskService;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

//    @PostMapping("/tasks")
//    public ResponseEntity<Task> createTask(@RequestBody Task task) {
//        User manager = getCurrentUser();
//        Task createdTask = taskService.createTask(task, manager.getId());
//        return ResponseEntity.ok(createdTask);
//    }
//
//    @PutMapping("/tasks/{taskId}")
//    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
//        Task updatedTask = taskService.updateTask(taskId, taskDetails);
//        return ResponseEntity.ok(updatedTask);
//    }
//
//    @DeleteMapping("/tasks/{taskId}")
//    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
//        taskService.deleteTask(taskId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/tasks/{taskId}/assignWorker/{workerId}")
//    public ResponseEntity<Task> assignWorker(@PathVariable Long taskId, @PathVariable Long workerId) {
//        Task task = taskService.assignWorker(taskId, workerId);
//        return ResponseEntity.ok(task);
//    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByManager() {
        User manager = getCurrentUser();
        List<Task> tasks = taskService.getTasksByManagerId(manager.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getManagerProfile() {
        User manager = getCurrentUser();
        return ResponseEntity.ok(manager);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateManagerProfile(@RequestBody User updatedManager) {
        User manager = userService.updateUser(updatedManager);
        return ResponseEntity.ok(manager);
    }
}
