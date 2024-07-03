package com.example.todo_service.entity;

import jakarta.persistence.*;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    private String username;

    @NotEmpty
    @NotBlank
    @NotNull
    @Size(min = 3, max = 20)
    private String password;

    @ElementCollection
    private Set<Role> roles;

    @Transient
    private Integer completedTaskCount = 0;

//    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
//    private Set<Task> managedTasks = new HashSet<>();
//
//    @ManyToMany(mappedBy = "workers", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Task> workingTasks = new HashSet<>();

    public enum Role implements GrantedAuthority {
        ROLE_MANAGER, ROLE_WORKER, ROLE_ADMIN;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }

    public void incrementCompletedTaskCount() {
            this.completedTaskCount += 1;
    }
}
