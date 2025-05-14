package com.br.sgpt.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "users")
public class UserEntity {
    @Id
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String role;

    public UserEntity() {}

    public UserEntity(UUID id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // getters e setters
    public UUID getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public String getRole() { return this.role; }

    public void setId(UUID id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
}