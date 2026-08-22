package com.lifesync.notification.scheduler;

import com.lifesync.notification.service.NotificationGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Runs the notification scan for every user automatically, once a day.
 * @EnableScheduling is already turned on in LifeSyncApplication (added back in Stage 1).
 *
 * Cron format: second minute hour day month weekday.
 * "0 0 8 * * *" = every day at 8:00 AM server time.
 *
 * For local testing you don't need to wait for this — POST /api/notifications/generate
 * triggers the same scan on demand for the logged-in user.
 */
@Component
public class NotificationScheduler {

    @Autowired
    private NotificationGeneratorService notificationGeneratorService;

    @Scheduled(cron = "0 0 8 * * *")
    public void runDailyNotificationScan() {
        notificationGeneratorService.generateForAllUsers();
    }
}