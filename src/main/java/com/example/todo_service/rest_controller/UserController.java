package com.example.todo_service.rest_controller;

import com.example.todo_service.entity.ErrorResponse;
import com.example.todo_service.entity.User;
import com.example.todo_service.security.auth.JWTTokenProvider;
import com.example.todo_service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTitle("Duplicate Username");
            errorResponse.setStatus(409);
            errorResponse.getErrors().add("Username already exists");
            return ResponseEntity.badRequest().body(errorResponse);
        }
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
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Wrong password");
        errorResponse.setStatus(401);
        errorResponse.getErrors().add("Wrong password");
        return ResponseEntity.badRequest().body(errorResponse.getTitle());
    }

    @GetMapping("/{username}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_WORKER"})
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        if(userService.existsByUsername(username)){
           User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Wrong username");
        errorResponse.setStatus(404);
        errorResponse.getErrors().add("User isn't exist");
        return ResponseEntity.badRequest().body(errorResponse);
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