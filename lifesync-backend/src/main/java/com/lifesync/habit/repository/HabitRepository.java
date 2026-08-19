package com.lifesync.habit.repository;

import com.lifesync.habit.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserIdAndActiveTrueOrderByTitleAsc(Long userId);

    Optional<Habit> findByIdAndUserId(Long id, Long userId);
}