package com.lifesync.goal.repository;

import com.lifesync.goal.entity.GoalMilestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalMilestoneRepository extends JpaRepository<GoalMilestone, Long> {

    List<GoalMilestone> findByGoalIdOrderByDisplayOrderAsc(Long goalId);

    Optional<GoalMilestone> findByIdAndGoalId(Long id, Long goalId);

    long countByGoalId(Long goalId);

    long countByGoalIdAndCompletedTrue(Long goalId);

    void deleteByGoalId(Long goalId);
}