package com.lifesync.habit.repository;

import com.lifesync.habit.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    List<HabitLog> findByHabitIdOrderByLogDateDesc(Long habitId);

    Optional<HabitLog> findByHabitIdAndLogDate(Long habitId, LocalDate logDate);

    long countByHabitIdAndCompletedTrue(Long habitId);

    void deleteByHabitId(Long habitId);
}