package com.lifesync.analytics.service;

import com.lifesync.analytics.dto.*;
import com.lifesync.finance.dto.FinanceAnalyticsResponse;
import com.lifesync.finance.service.FinanceAnalyticsService;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.service.GoalService;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.service.HabitService;
import com.lifesync.notification.controller.NotificationController;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.entity.TaskStatus;
import com.lifesync.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final NotificationController notificationController;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private FinanceAnalyticsService financeAnalyticsService;

    @Autowired
    private InsightEngine insightEngine;

    AnalyticsServiceImpl(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    @Override
    public LifeAnalyticsResponse getOverview(Long userId) {

        ProductivityStats productivity = buildProductivityStats(userId);
        GoalConsistencyStats goals = buildGoalStats(userId);
        HabitConsistencyStats habits = buildHabitStats(userId);
        FinanceSnapshot finance = buildFinanceSnapshot(userId);

        LifeAnalyticsResponse response = new LifeAnalyticsResponse();
        response.setGeneratedOn(LocalDate.now());
        response.setProductivity(productivity);
        response.setGoals(goals);
        response.setHabits(habits);
        response.setFinance(finance);
        response.setInsights(insightEngine.generateInsights(userId, productivity, goals, habits, finance));

        return response;
    }

    private ProductivityStats buildProductivityStats(Long userId) {
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = LocalDate.now().with(DayOfWeek.SUNDAY);

        List<TaskResponse> allTasks = taskService.getAll(userId);

        long plannedThisWeek = allTasks.stream()
                .filter(t -> t.getDueDate() != null
                        && !t.getDueDate().isBefore(weekStart)
                        && !t.getDueDate().isAfter(weekEnd))
                .count();

        long completedThisWeek = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE && t.getCompletedAt() != null)
                .filter(t -> {
                    LocalDate completedDate = t.getCompletedAt().toLocalDate();
                    return !completedDate.isBefore(weekStart) && !completedDate.isAfter(weekEnd);
                })
                .count();

        double completionRate = plannedThisWeek == 0 ? 0.0
                : round(completedThisWeek * 100.0 / plannedThisWeek);

        ProductivityStats stats = new ProductivityStats();
        stats.setTasksPlannedThisWeek((int) plannedThisWeek);
        stats.setTasksCompletedThisWeek((int) completedThisWeek);
        stats.setCompletionRatePercentage(completionRate);
        stats.setOverdueTasksCount(taskService.getOverdue(userId).size());
        return stats;
    }

    private GoalConsistencyStats buildGoalStats(Long userId) {
        List<GoalResponse> allGoals = goalService.getAll(userId);

        GoalConsistencyStats stats = new GoalConsistencyStats();
        stats.setTotalGoals(allGoals.size());
        stats.setOnTrackCount((int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.ON_TRACK).count());
        stats.setAtRiskCount((int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.AT_RISK).count());
        stats.setOverdueCount((int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.OVERDUE).count());
        stats.setCompletedCount((int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.COMPLETED).count());

        double avgProgress = allGoals.isEmpty() ? 0.0
                : round(allGoals.stream().mapToDouble(GoalResponse::getProgressPercentage).average().orElse(0.0));
        stats.setAverageProgressPercentage(avgProgress);

        return stats;
    }

    private HabitConsistencyStats buildHabitStats(Long userId) {
        List<HabitResponse> activeHabits = habitService.getAll(userId); // already active-only

        HabitConsistencyStats stats = new HabitConsistencyStats();
        stats.setTotalActiveHabits(activeHabits.size());
        stats.setHabitsWithActiveStreak((int) activeHabits.stream().filter(h -> h.getCurrentStreak() > 0).count());

        double avgAdherence = activeHabits.isEmpty() ? 0.0
                : round(activeHabits.stream().mapToDouble(HabitResponse::getAdherencePercentage).average().orElse(0.0));
        stats.setAverageAdherencePercentage(avgAdherence);

        return stats;
    }

    private FinanceSnapshot buildFinanceSnapshot(Long userId) {
        FinanceAnalyticsResponse monthly = financeAnalyticsService.getMonthlyAnalytics(userId, YearMonth.now());

        FinanceSnapshot snapshot = new FinanceSnapshot();
        snapshot.setTotalIncome(monthly.getTotalIncome());
        snapshot.setTotalExpense(monthly.getTotalExpense());
        snapshot.setNetSavings(monthly.getNetSavings());

        if (monthly.getTotalIncome().compareTo(BigDecimal.ZERO) > 0) {
            double rate = monthly.getNetSavings()
                    .divide(monthly.getTotalIncome(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            snapshot.setSavingsRatePercentage(rate);
        }

        return snapshot;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}