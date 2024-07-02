package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.User;
import com.example.todo_service.security.auth.JWTTokenProvider;
import com.example.todo_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User add = userService.add(user);
        return ResponseEntity.ok(add);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User byUsername = userService.findByUsername(user.getUsername());
        if (new BCryptPasswordEncoder().matches(user.getPassword(), byUsername.getPassword())) {
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

    //TODO update service,
}