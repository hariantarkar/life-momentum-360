package com.lifesync.task.repository;

import com.lifesync.task.entity.Task;
import com.lifesync.task.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdOrderByDueDateAsc(Long userId);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    List<Task> findByUserIdAndStatusOrderByDueDateAsc(Long userId, TaskStatus status);

    List<Task> findByUserIdAndGoalIdOrderByDueDateAsc(Long userId, Long goalId);

    List<Task> findByUserIdAndMilestoneIdOrderByDueDateAsc(Long userId, Long milestoneId);

    List<Task> findByUserIdAndDueDateLessThanAndStatusNot(Long userId, LocalDate date, TaskStatus excludeStatus);

    List<Task> findByUserIdAndDueDate(Long userId, LocalDate date);
}