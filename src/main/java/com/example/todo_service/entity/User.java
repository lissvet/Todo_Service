package com.example.todo_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ElementCollection
    private Set<Role> roles;

    @Transient
    private Integer completedTaskCount = 0;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private Set<Task> managedTasks = new HashSet<>();

    @ManyToMany(mappedBy = "workers", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> workingTasks = new HashSet<>();

    public enum Role implements GrantedAuthority {
        ROLE_MANAGER, ROLE_WORKER, ROLE_ADMIN;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }

    public void incrementCompletedTaskCount() {
        if (roles.contains(Role.ROLE_WORKER)) {
            this.completedTaskCount += 1;
        }
    }
}
