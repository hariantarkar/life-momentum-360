package com.lifesync.notification.repository;

import com.lifesync.notification.entity.Notification;
import com.lifesync.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndReadFalse(Long userId);

    /** Dedup check: has this exact source+type notification already been created today? */
    boolean existsByUserIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtGreaterThanEqual(
            Long userId, String sourceType, Long sourceId, NotificationType type, LocalDateTime since);
}