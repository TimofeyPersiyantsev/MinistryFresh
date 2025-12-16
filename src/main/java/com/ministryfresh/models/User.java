package com.ministryfresh.models;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role; // COMPANY, EMPLOYMENT_CENTER, CITIZEN
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User() {}

    public User(String username, String email, String password, String fullName, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public static final String ROLE_COMPANY = "COMPANY";
    public static final String ROLE_EMPLOYMENT_CENTER = "EMPLOYMENT_CENTER";
    public static final String ROLE_CITIZEN = "CITIZEN";

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public boolean isCompany() { return ROLE_COMPANY.equals(role); }
    public boolean isEmploymentCenter() { return ROLE_EMPLOYMENT_CENTER.equals(role); }
    public boolean isCitizen() { return ROLE_CITIZEN.equals(role); }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', fullName='%s', role='%s'}",
                id, username, email, fullName, role);
    }
}