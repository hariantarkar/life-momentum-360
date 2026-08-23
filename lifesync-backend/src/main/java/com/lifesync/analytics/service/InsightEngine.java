package com.lifesync.analytics.service;

import com.lifesync.analytics.dto.*;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.service.GoalService;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule-based insight generator — turns raw stats into plain-English observations.
 * No external AI/LLM call, per the product spec's "rule-based insights, not an
 * external AI API" requirement. Every rule here is a simple, explainable if/else,
 * intentionally readable enough to explain in an interview.
 */
@Component
public class InsightEngine {

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;

    public List<Insight> generateInsights(Long userId, ProductivityStats productivity,
                                           GoalConsistencyStats goals, HabitConsistencyStats habits,
                                           FinanceSnapshot finance) {
        List<Insight> insights = new ArrayList<>();

        addGoalInsights(insights, userId);
        addTaskInsights(insights, productivity);
        addHabitInsights(insights, userId);
        addFinanceInsights(insights, finance);

        if (insights.isEmpty()) {
            insights.add(new Insight("GENERAL", InsightSeverity.POSITIVE,
                    "Everything looks on track right now — no urgent flags."));
        }

        return insights;
    }

    /** Directly mirrors the product spec's example: "deadline is 10 days away and only 55% of milestones are complete." */
    private void addGoalInsights(List<Insight> insights, Long userId) {
        List<GoalResponse> allGoals = goalService.getAll(userId);

        for (GoalResponse goal : allGoals) {
            if (goal.getHealth() == GoalHealth.AT_RISK) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getTargetDate());
                insights.add(new Insight("GOAL", InsightSeverity.WARNING,
                        "Your goal \"" + goal.getTitle() + "\" deadline is " + daysLeft + " day(s) away and only "
                                + goal.getProgressPercentage() + "% of milestones are complete."));
            } else if (goal.getHealth() == GoalHealth.OVERDUE) {
                insights.add(new Insight("GOAL", InsightSeverity.WARNING,
                        "\"" + goal.getTitle() + "\" has passed its target date of " + goal.getTargetDate()
                                + " and is still not complete."));
            } else if (goal.getHealth() == GoalHealth.COMPLETED) {
                insights.add(new Insight("GOAL", InsightSeverity.POSITIVE,
                        "You completed \"" + goal.getTitle() + "\" — nice work!"));
            }
        }
    }

    private void addTaskInsights(List<Insight> insights, ProductivityStats productivity) {
        if (productivity.getOverdueTasksCount() > 0) {
            insights.add(new Insight("TASK", InsightSeverity.WARNING,
                    "You have " + productivity.getOverdueTasksCount()
                            + " overdue task(s). Consider rescheduling or breaking them into smaller steps."));
        }

        if (productivity.getTasksPlannedThisWeek() > 0) {
            double rate = productivity.getCompletionRatePercentage();
            if (rate >= 80) {
                insights.add(new Insight("TASK", InsightSeverity.POSITIVE,
                        "Great week — you've completed " + rate + "% of the tasks due this week."));
            } else if (rate < 50) {
                insights.add(new Insight("TASK", InsightSeverity.INFO,
                        "You've completed " + rate + "% of this week's planned tasks so far. "
                                + "Try prioritizing your top 3 tasks each day."));
            }
        }
    }

    private void addHabitInsights(List<Insight> insights, Long userId) {
        List<HabitResponse> activeHabits = habitService.getAll(userId);

        for (HabitResponse habit : activeHabits) {
            if (habit.getCurrentStreak() >= 7) {
                insights.add(new Insight("HABIT", InsightSeverity.POSITIVE,
                        "You're on a " + habit.getCurrentStreak() + "-day streak for \"" + habit.getTitle() + "\" — keep it going!"));
            }

            // Only flag low adherence for habits that have existed long enough for the number to mean something
            long daysSinceCreated = ChronoUnit.DAYS.between(habit.getCreatedAt().toLocalDate(), LocalDate.now());
            if (daysSinceCreated >= 7 && habit.getAdherencePercentage() < 50) {
                insights.add(new Insight("HABIT", InsightSeverity.INFO,
                        "Your adherence for \"" + habit.getTitle() + "\" is " + habit.getAdherencePercentage()
                                + "%. Consider adjusting the routine or reminder time."));
            }
        }
    }

    private void addFinanceInsights(List<Insight> insights, FinanceSnapshot finance) {
        if (finance.getNetSavings() != null && finance.getNetSavings().compareTo(BigDecimal.ZERO) < 0) {
            insights.add(new Insight("FINANCE", InsightSeverity.WARNING,
                    "You're spending more than you're earning this month (net: " + finance.getNetSavings() + ")."));
        } else if (finance.getSavingsRatePercentage() != null && finance.getSavingsRatePercentage() >= 20) {
            insights.add(new Insight("FINANCE", InsightSeverity.POSITIVE,
                    "You're saving " + finance.getSavingsRatePercentage() + "% of your income this month — solid progress."));
        }
    }
}


