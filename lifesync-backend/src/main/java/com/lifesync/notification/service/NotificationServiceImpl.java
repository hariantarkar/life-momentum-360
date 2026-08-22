package com.lifesync.notification.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.notification.dto.NotificationResponse;
import com.lifesync.notification.entity.Notification;
import com.lifesync.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getAll(Long userId, boolean unreadOnly) {
        List<Notification> notifications = unreadOnly
                ? notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                : notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long userId, Long notificationId) {
        Notification notification = getOwned(userId, notificationId);
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long notificationId) {
        notificationRepository.delete(getOwned(userId, notificationId));
    }

    private Notification getOwned(Long userId, Long notificationId) {
        return notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    private NotificationResponse toResponse(Notification n) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(n.getId());
        dto.setType(n.getType());
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setSourceType(n.getSourceType());
        dto.setSourceId(n.getSourceId());
        dto.setRead(n.isRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}