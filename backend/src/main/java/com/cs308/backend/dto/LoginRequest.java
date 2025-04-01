package com.cs308.backend.dto;

import com.cs308.backend.dao.Role;

public class LoginRequest {
    private String email;
    private String password;
    private Role role;

    public LoginRequest() {
        this.email = null;
        this.password = null;
        this.role = null;
    }

    public LoginRequest(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
