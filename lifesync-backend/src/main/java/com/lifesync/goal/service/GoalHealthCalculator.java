package com.lifesync.goal.service;

import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.entity.GoalStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Rule-based goal health engine.
 * Example from the product spec: "deadline is 10 days away and only 55% of
 * milestones are complete" -> AT_RISK.
 *
 * Rules (in priority order):
 *   1. status == COMPLETED         -> COMPLETED
 *   2. targetDate has passed       -> OVERDUE
 *   3. <= 10 days left AND progress < 60% -> AT_RISK
 *   4. otherwise                   -> ON_TRACK
 */
@Component
public class GoalHealthCalculator {

    private static final int AT_RISK_DAYS_THRESHOLD = 10;
    private static final double AT_RISK_PROGRESS_THRESHOLD = 60.0;

    public GoalHealth calculate(GoalStatus status, LocalDate targetDate, double progressPercentage) {

        if (status == GoalStatus.COMPLETED) {
            return GoalHealth.COMPLETED;
        }

        LocalDate today = LocalDate.now();

        if (targetDate.isBefore(today)) {
            return GoalHealth.OVERDUE;
        }

        long daysRemaining = ChronoUnit.DAYS.between(today, targetDate);

        if (daysRemaining <= AT_RISK_DAYS_THRESHOLD && progressPercentage < AT_RISK_PROGRESS_THRESHOLD) {
            return GoalHealth.AT_RISK;
        }

        return GoalHealth.ON_TRACK;
    }

    public double calculateProgress(long totalMilestones, long completedMilestones) {
        if (totalMilestones == 0) {
            return 0.0;
        }
        return Math.round((completedMilestones * 100.0 / totalMilestones) * 100.0) / 100.0; // 2 decimal places
    }
}