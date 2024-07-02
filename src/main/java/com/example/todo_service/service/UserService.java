package com.example.todo_service.service;

import com.example.todo_service.entity.Manager;
import com.example.todo_service.entity.User;
import com.example.todo_service.entity.Worker;
import com.example.todo_service.repository.ManagerRepository;
import com.example.todo_service.repository.UserRepository;
import com.example.todo_service.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ManagerRepository managerRepository;

    public User add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
    public User addManager(Manager user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return managerRepository.save(user);
    }
    public User addWorker(Worker user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return workerRepository.save(user);
    }

    public Manager findManagerByUsername(String username) {
        return managerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public Worker findWorkerByUsername(String username) {
        return workerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(User userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDetails.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.delete(user);
    }


}