package com.lifesync.audit.service;

import com.lifesync.audit.dto.AuditLogResponse;

import java.util.List;

public interface AuditService {
    void log(Long userId, String action, String details);
    void logFailedAttempt(String attemptedEmail, String action, String details);
    List<AuditLogResponse> getMyLogs(Long userId);
}