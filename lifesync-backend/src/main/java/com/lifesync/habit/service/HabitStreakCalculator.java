package com.lifesync.habit.service;

import com.lifesync.habit.entity.Habit;
import com.lifesync.habit.entity.HabitFrequency;
import com.lifesync.habit.entity.HabitLog;
import com.lifesync.habit.repository.HabitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Streak + adherence engine.
 *
 * Key design point (interview question: "how do you calculate streaks correctly
 * when a day is missed?"): a habit log is only ever CREATED on a completed day —
 * there's no "missed" row. So a streak is just "how many consecutive expected
 * periods (days or weeks), counting backward, have a log?" The moment a period
 * has no log, the streak stops. Today (or this week) gets a grace period: if it
 * hasn't been logged yet, we don't break the streak for it — we just start
 * counting from yesterday (or last week) instead, since the day/week isn't over.
 */
@Component
public class HabitStreakCalculator {

    @Autowired
    private HabitLogRepository habitLogRepository;

    public int calculateCurrentStreak(Habit habit) {
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByLogDateDesc(habit.getId());
        if (logs.isEmpty()) {
            return 0;
        }

        Set<LocalDate> loggedDates = new HashSet<>();
        for (HabitLog log : logs) {
            if (log.isCompleted()) {
                loggedDates.add(log.getLogDate());
            }
        }

        if (habit.getFrequency() == HabitFrequency.DAILY) {
            return calculateDailyStreak(loggedDates);
        } else {
            return calculateWeeklyStreak(loggedDates);
        }
    }

    private int calculateDailyStreak(Set<LocalDate> loggedDates) {
        LocalDate cursor = LocalDate.now();

        if (!loggedDates.contains(cursor)) {
            cursor = cursor.minusDays(1); // grace: today not logged yet, start from yesterday
        }

        int streak = 0;
        while (loggedDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private int calculateWeeklyStreak(Set<LocalDate> loggedDates) {
        Set<String> loggedWeeks = new HashSet<>();
        for (LocalDate date : loggedDates) {
            loggedWeeks.add(weekKey(date));
        }

        LocalDate cursor = LocalDate.now();
        if (!loggedWeeks.contains(weekKey(cursor))) {
            cursor = cursor.minusWeeks(1); // grace: this week not logged yet
        }

        int streak = 0;
        while (loggedWeeks.contains(weekKey(cursor))) {
            streak++;
            cursor = cursor.minusWeeks(1);
        }
        return streak;
    }

    private String weekKey(LocalDate date) {
        int year = date.get(IsoFields.WEEK_BASED_YEAR);
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return year + "-W" + week;
    }

    /**
     * Adherence % = completed periods (days/weeks) since the habit was created,
     * divided by total expected periods since creation.
     */
    public double calculateAdherencePercentage(Habit habit) {
        LocalDate createdDate = habit.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        if (habit.getFrequency() == HabitFrequency.DAILY) {
            long totalDays = ChronoUnit.DAYS.between(createdDate, today) + 1;
            long completedDays = habitLogRepository.countByHabitIdAndCompletedTrue(habit.getId());
            return round(Math.min(completedDays, totalDays) * 100.0 / totalDays);
        } else {
            long totalWeeks = ChronoUnit.WEEKS.between(createdDate, today) + 1;
            List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByLogDateDesc(habit.getId());
            Set<String> completedWeeks = new HashSet<>();
            for (HabitLog log : logs) {
                if (log.isCompleted()) {
                    completedWeeks.add(weekKey(log.getLogDate()));
                }
            }
            return round(Math.min(completedWeeks.size(), totalWeeks) * 100.0 / totalWeeks);
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}