package com.lifesync.auth.dto;

import com.lifesync.user.entity.User;

public class UserSummary {

    private Long id;
    private String fullName;
    private String email;
    private String role;

    public UserSummary() {
    }

    public UserSummary(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public static UserSummary from(User user) {
        return new UserSummary(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
