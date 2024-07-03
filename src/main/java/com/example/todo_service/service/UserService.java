package com.example.todo_service.service;

import com.example.todo_service.entity.User;
import com.example.todo_service.repository.UserRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }

    @Secured({"ROLE_ADMIN"})
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Secured({"ROLE_ADMIN"})
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(User updatedUser) {
        return userRepository.findById(updatedUser.getId()).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
            user.setRoles(updatedUser.getRoles());
            return userRepository.save(user);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
