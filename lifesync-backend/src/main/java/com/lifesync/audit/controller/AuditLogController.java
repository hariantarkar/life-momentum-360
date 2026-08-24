package com.lifesync.audit.controller;

import com.lifesync.audit.dto.AuditLogResponse;
import com.lifesync.audit.service.AuditService;
import com.lifesync.common.response.ApiResponse;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Self-service transparency: users can see their own security activity, nobody else's. */
@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditService auditService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getMyLogs(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(auditService.getMyLogs(principal.getId())));
    }
}