package com.lifesync.goal.service;

import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.entity.GoalStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pure unit tests — GoalHealthCalculator has no dependencies to mock.
 * Directly verifies the rule-based health engine described in the product spec:
 * "deadline is 10 days away and only 55% of milestones are complete" -> AT_RISK.
 */
class GoalHealthCalculatorTest {

    private final GoalHealthCalculator calculator = new GoalHealthCalculator();

    @Test
    void completedStatus_alwaysReturnsCompleted_regardlessOfDateOrProgress() {
        GoalHealth health = calculator.calculate(GoalStatus.COMPLETED, LocalDate.now().minusDays(5), 10.0);
        assertThat(health).isEqualTo(GoalHealth.COMPLETED);
    }

    @Test
    void targetDateInThePast_andNotCompleted_returnsOverdue() {
        GoalHealth health = calculator.calculate(GoalStatus.IN_PROGRESS, LocalDate.now().minusDays(1), 90.0);
        assertThat(health).isEqualTo(GoalHealth.OVERDUE);
    }

    @Test
    void closeDeadlineWithLowProgress_returnsAtRisk() {
        // Mirrors the product spec's own example: 10 days away, 55% complete -> AT_RISK
        GoalHealth health = calculator.calculate(GoalStatus.IN_PROGRESS, LocalDate.now().plusDays(10), 55.0);
        assertThat(health).isEqualTo(GoalHealth.AT_RISK);
    }

    @Test
    void closeDeadlineWithHighProgress_returnsOnTrack() {
        // Same 10-day window, but progress is above the 60% threshold this time
        GoalHealth health = calculator.calculate(GoalStatus.IN_PROGRESS, LocalDate.now().plusDays(10), 75.0);
        assertThat(health).isEqualTo(GoalHealth.ON_TRACK);
    }

    @Test
    void farDeadlineWithLowProgress_returnsOnTrack() {
        // Low progress alone isn't enough to flag AT_RISK if the deadline isn't close yet
        GoalHealth health = calculator.calculate(GoalStatus.IN_PROGRESS, LocalDate.now().plusDays(60), 5.0);
        assertThat(health).isEqualTo(GoalHealth.ON_TRACK);
    }

    @Test
    void elevenDaysRemaining_isJustOutsideAtRiskWindow() {
        // Boundary test: the threshold is "<= 10 days", so 11 days should NOT trigger AT_RISK
        GoalHealth health = calculator.calculate(GoalStatus.IN_PROGRESS, LocalDate.now().plusDays(11), 10.0);
        assertThat(health).isEqualTo(GoalHealth.ON_TRACK);
    }

    @Test
    void calculateProgress_returnsZero_whenNoMilestones() {
        double progress = calculator.calculateProgress(0, 0);
        assertThat(progress).isZero();
    }

    @Test
    void calculateProgress_roundsToTwoDecimalPlaces() {
        // 1 of 3 milestones complete = 33.333...% -> should round to 33.33
        double progress = calculator.calculateProgress(3, 1);
        assertThat(progress).isEqualTo(33.33);
    }

    @Test
    void calculateProgress_allComplete_returnsOneHundred() {
        double progress = calculator.calculateProgress(4, 4);
        assertThat(progress).isEqualTo(100.0);
    }
}