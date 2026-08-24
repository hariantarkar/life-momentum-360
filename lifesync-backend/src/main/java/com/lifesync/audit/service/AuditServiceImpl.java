package com.lifesync.audit.service;

import com.lifesync.audit.dto.AuditLogResponse;
import com.lifesync.audit.entity.AuditLog;
import com.lifesync.audit.repository.AuditLogRepository;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void log(Long userId, String action, String details) {
        AuditLog entry = new AuditLog();
        entry.setUser(userRepository.getReferenceById(userId));
        entry.setAction(action);
        entry.setDetails(details);
        entry.setIpAddress(getClientIp());
        auditLogRepository.save(entry);
    }

    @Override
    @Transactional
    public void logFailedAttempt(String attemptedEmail, String action, String details) {
        AuditLog entry = new AuditLog();
        entry.setUser(null);
        entry.setAttemptedEmail(attemptedEmail);
        entry.setAction(action);
        entry.setDetails(details);
        entry.setIpAddress(getClientIp());
        auditLogRepository.save(entry);
    }

    @Override
    public List<AuditLogResponse> getMyLogs(Long userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Pulls the current HTTP request out of Spring's thread-local context, so callers
     * (like AuthServiceImpl) don't need to pass HttpServletRequest through every method
     * signature just to log an IP address.
     */
    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest().getRemoteAddr();
    }

    private AuditLogResponse toResponse(AuditLog entry) {
        AuditLogResponse dto = new AuditLogResponse();
        dto.setId(entry.getId());
        dto.setAction(entry.getAction());
        dto.setDetails(entry.getDetails());
        dto.setIpAddress(entry.getIpAddress());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
}