package com.lifesync.habit.service;

import com.lifesync.habit.entity.Habit;
import com.lifesync.habit.entity.HabitFrequency;
import com.lifesync.habit.entity.HabitLog;
import com.lifesync.habit.repository.HabitLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Tests the streak "grace period" logic described in the class-level Javadoc:
 * today (or this week) not being logged yet should NOT break the streak — it
 * should just start counting from yesterday instead. This is the interview
 * question "how do you calculate streaks correctly when a day is missed?"
 */
@ExtendWith(MockitoExtension.class)
class HabitStreakCalculatorTest {

    @Mock
    private HabitLogRepository habitLogRepository;

    @InjectMocks
    private HabitStreakCalculator calculator;

    private Habit dailyHabit;

    @BeforeEach
    void setUp() {
        dailyHabit = new Habit();
        dailyHabit.setId(1L);
        dailyHabit.setFrequency(HabitFrequency.DAILY);
        // createdAt has no public setter (managed by @PrePersist) — reflection is the
        // standard, clean way to set it directly in a unit test.
        ReflectionTestUtils.setField(dailyHabit, "createdAt", LocalDateTime.now().minusDays(10));
    }

    private HabitLog logFor(LocalDate date) {
        HabitLog log = new HabitLog();
        log.setLogDate(date);
        log.setCompleted(true);
        return log;
    }

    @Test
    void noLogsAtAll_streakIsZero() {
        when(habitLogRepository.findByHabitIdOrderByLogDateDesc(1L)).thenReturn(new ArrayList<>());

        int streak = calculator.calculateCurrentStreak(dailyHabit);

        assertThat(streak).isZero();
    }

    @Test
    void loggedTodayAndPastTwoDays_streakIsThree() {
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = List.of(
                logFor(today),
                logFor(today.minusDays(1)),
                logFor(today.minusDays(2))
        );
        when(habitLogRepository.findByHabitIdOrderByLogDateDesc(1L)).thenReturn(logs);

        int streak = calculator.calculateCurrentStreak(dailyHabit);

        assertThat(streak).isEqualTo(3);
    }

    @Test
    void notLoggedToday_butLoggedYesterday_streakStillCountsGracePeriod() {
        // This is the key grace-period behavior: today missing shouldn't zero out the streak
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = List.of(
                logFor(today.minusDays(1)),
                logFor(today.minusDays(2))
        );
        when(habitLogRepository.findByHabitIdOrderByLogDateDesc(1L)).thenReturn(logs);

        int streak = calculator.calculateCurrentStreak(dailyHabit);

        assertThat(streak).isEqualTo(2); // yesterday + day before, today's absence forgiven
    }

    @Test
    void gapInTheMiddle_breaksTheStreak() {
        // Logged today and 2 days ago, but NOT yesterday — the gap should cut the streak to 1
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = List.of(
                logFor(today),
                logFor(today.minusDays(2)) // yesterday is missing
        );
        when(habitLogRepository.findByHabitIdOrderByLogDateDesc(1L)).thenReturn(logs);

        int streak = calculator.calculateCurrentStreak(dailyHabit);

        assertThat(streak).isEqualTo(1); // only today counts; the streak stops at the gap
    }

    @Test
    void adherencePercentage_100PercentWhenLoggedEveryDaySinceCreation() {
        // Habit created 10 days ago (see setUp), logged all 11 days (today inclusive)
        when(habitLogRepository.countByHabitIdAndCompletedTrue(1L)).thenReturn(11L);

        double adherence = calculator.calculateAdherencePercentage(dailyHabit);

        assertThat(adherence).isEqualTo(100.0);
    }

    @Test
    void adherencePercentage_partialCompletion() {
        // 11 expected days (created 10 days ago + today), only 5 logged
        when(habitLogRepository.countByHabitIdAndCompletedTrue(1L)).thenReturn(5L);

        double adherence = calculator.calculateAdherencePercentage(dailyHabit);

        assertThat(adherence).isCloseTo(45.45, org.assertj.core.data.Offset.offset(0.01));
    }
}