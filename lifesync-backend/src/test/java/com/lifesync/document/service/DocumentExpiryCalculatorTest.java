package com.lifesync.document.service;

import com.lifesync.document.entity.DocumentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentExpiryCalculatorTest {

    private final DocumentExpiryCalculator calculator = new DocumentExpiryCalculator();

    @Test
    void nullExpiryDate_returnsNoExpiry() {
        DocumentStatus status = calculator.calculateStatus(null, 30);
        assertThat(status).isEqualTo(DocumentStatus.NO_EXPIRY);
    }

    @Test
    void expiryDateInThePast_returnsExpired() {
        DocumentStatus status = calculator.calculateStatus(LocalDate.now().minusDays(1), 30);
        assertThat(status).isEqualTo(DocumentStatus.EXPIRED);
    }

    @Test
    void withinReminderWindow_returnsExpiringSoon() {
        // 15 days out, reminder window is 30 days -> should flag as expiring soon
        DocumentStatus status = calculator.calculateStatus(LocalDate.now().plusDays(15), 30);
        assertThat(status).isEqualTo(DocumentStatus.EXPIRING_SOON);
    }

    @Test
    void wellBeyondReminderWindow_returnsActive() {
        DocumentStatus status = calculator.calculateStatus(LocalDate.now().plusDays(90), 30);
        assertThat(status).isEqualTo(DocumentStatus.ACTIVE);
    }

    @Test
    void exactlyAtReminderThreshold_isInclusive() {
        // The rule is "<= renewalReminderDays", so exactly 30 days out should already be EXPIRING_SOON
        DocumentStatus status = calculator.calculateStatus(LocalDate.now().plusDays(30), 30);
        assertThat(status).isEqualTo(DocumentStatus.EXPIRING_SOON);
    }

    @Test
    void customShortReminderWindow_respectsConfiguredValue() {
        // A 7-day reminder window: 10 days out should be ACTIVE, not EXPIRING_SOON
        DocumentStatus status = calculator.calculateStatus(LocalDate.now().plusDays(10), 7);
        assertThat(status).isEqualTo(DocumentStatus.ACTIVE);
    }

    @Test
    void calculateDaysUntilExpiry_nullDate_returnsNull() {
        assertThat(calculator.calculateDaysUntilExpiry(null)).isNull();
    }

    @Test
    void calculateDaysUntilExpiry_futureDate_returnsPositiveCount() {
        Long days = calculator.calculateDaysUntilExpiry(LocalDate.now().plusDays(5));
        assertThat(days).isEqualTo(5L);
    }

    @Test
    void calculateDaysUntilExpiry_pastDate_returnsNegativeCount() {
        Long days = calculator.calculateDaysUntilExpiry(LocalDate.now().minusDays(3));
        assertThat(days).isEqualTo(-3L);
    }
}