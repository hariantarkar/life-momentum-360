package com.lifesync.notification.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.notification.dto.NotificationResponse;
import com.lifesync.notification.service.NotificationGeneratorService;
import com.lifesync.notification.service.NotificationService;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationGeneratorService notificationGeneratorService;

    /** Optional ?unreadOnly=true filter. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {

        return ResponseEntity.ok(ApiResponse.success(notificationService.getAll(principal.getId(), unreadOnly)));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(Map.of("unreadCount", notificationService.getUnreadCount(principal.getId()))));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success("Marked as read", notificationService.markAsRead(principal.getId(), id)));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {

        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        notificationService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted", null));
    }

    /** Manually trigger the scan for the logged-in user — mainly for testing/demo, since the real job runs daily via the scheduler. */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> generate(
            @AuthenticationPrincipal UserPrincipal principal) {

        int created = notificationGeneratorService.generateForUser(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Notification scan complete", Map.of("notificationsCreated", created)));
    }
}