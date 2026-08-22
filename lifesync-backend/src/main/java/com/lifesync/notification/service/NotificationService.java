package com.lifesync.notification.service;

import com.lifesync.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getAll(Long userId, boolean unreadOnly);
    long getUnreadCount(Long userId);
    NotificationResponse markAsRead(Long userId, Long notificationId);
    void markAllAsRead(Long userId);
    void delete(Long userId, Long notificationId);
}