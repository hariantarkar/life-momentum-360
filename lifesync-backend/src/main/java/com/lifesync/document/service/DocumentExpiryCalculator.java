package com.lifesync.document.service;

import com.lifesync.document.entity.DocumentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class DocumentExpiryCalculator {

    public DocumentStatus calculateStatus(LocalDate expiryDate, int renewalReminderDays) {
        if (expiryDate == null) {
            return DocumentStatus.NO_EXPIRY;
        }

        LocalDate today = LocalDate.now();

        if (expiryDate.isBefore(today)) {
            return DocumentStatus.EXPIRED;
        }

        long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
        if (daysUntilExpiry <= renewalReminderDays) {
            return DocumentStatus.EXPIRING_SOON;
        }

        return DocumentStatus.ACTIVE;
    }

    public Long calculateDaysUntilExpiry(LocalDate expiryDate) {
        if (expiryDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
}