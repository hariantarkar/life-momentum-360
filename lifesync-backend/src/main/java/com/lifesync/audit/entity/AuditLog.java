package com.lifesync.audit.entity;

import com.lifesync.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // nullable — a failed login before the user is resolved has no user id yet
    private User user;

    @Column(length = 150)
    private String attemptedEmail; // set only when user is null (e.g. failed login with unknown email)

    @Column(nullable = false, length = 40)
    private String action; // e.g. "LOGIN_SUCCESS", "LOGIN_FAILED", "REGISTER", "LOGOUT", "TOKEN_REFRESH"

    @Column(length = 500)
    private String details;

    @Column(length = 60)
    private String ipAddress;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAttemptedEmail() {
        return attemptedEmail;
    }

    public void setAttemptedEmail(String attemptedEmail) {
        this.attemptedEmail = attemptedEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}