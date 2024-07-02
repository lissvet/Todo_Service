package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.Manager;
import com.example.todo_service.entity.User;
import com.example.todo_service.entity.Worker;
import com.example.todo_service.repository.ManagerRepository;
import com.example.todo_service.repository.UserRepository;
import com.example.todo_service.repository.WorkerRepository;
import com.example.todo_service.security.auth.JWTTokenProvider;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        User add = userService.add(user);
//        return ResponseEntity.ok(add);
//    }

    @PostMapping("/manager")
    public ResponseEntity<User> createManager(@RequestBody Manager user) {
        User add = userService.addManager(user);
        return ResponseEntity.ok(add);
    }

    @PostMapping("/worker")
    public ResponseEntity<User> createWorker(@RequestBody Worker user) {
        User add = userService.addWorker(user);
        return ResponseEntity.ok(add);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        User byUsername = userService.findByUsername(user.getUsername());
//        if (new BCryptPasswordEncoder().matches(user.getPassword(), byUsername.getPassword())) {
//            String token = jwtTokenProvider.generateToken(byUsername.getId(), byUsername.getUsername(), byUsername.getPassword(), byUsername.getRoles());
//            return ResponseEntity.ok(token);
//        }
//        throw new RuntimeException();
//    }

    @PostMapping("/login_manager")
    public ResponseEntity<String> login(@RequestBody Manager manager) {
        Manager byUsername = userService.findManagerByUsername(manager.getUsername());
        if (new BCryptPasswordEncoder().matches(manager.getPassword(), byUsername.getPassword())) {
            String token = jwtTokenProvider.generateToken(byUsername.getId(), byUsername.getUsername(), byUsername.getPassword(), byUsername.getRoles());
            return ResponseEntity.ok(token);
        }
        throw new RuntimeException();
    }

    @PostMapping("/login_worker")
    public ResponseEntity<String> login(Worker worker) {
        Worker byUsername = userService.findWorkerByUsername(worker.getUsername());
        if (new BCryptPasswordEncoder().matches(worker.getPassword(), byUsername.getPassword())) {
            String token = jwtTokenProvider.generateToken(byUsername.getId(), byUsername.getUsername(), byUsername.getPassword(), byUsername.getRoles());
            return ResponseEntity.ok(token);
        }
        throw new RuntimeException();
    }

    @GetMapping("/{username}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}