package com.lifesync.goal.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.dto.MilestoneRequest;
import com.lifesync.goal.dto.MilestoneResponse;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.entity.GoalMilestone;
import com.lifesync.goal.entity.GoalStatus;
import com.lifesync.goal.repository.GoalMilestoneRepository;
import com.lifesync.goal.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MilestoneServiceImpl implements MilestoneService {

    @Autowired
    private GoalMilestoneRepository milestoneRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Override
    @Transactional
    public MilestoneResponse create(Long userId, Long goalId, MilestoneRequest request) {
        Goal goal = getOwnedGoal(userId, goalId);

        GoalMilestone milestone = new GoalMilestone();
        milestone.setGoal(goal);
        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setTargetDate(request.getTargetDate());
        milestone.setDisplayOrder(request.getDisplayOrder());
        milestone.setCompleted(false);

        GoalMilestone saved = milestoneRepository.save(milestone);

        // A goal moves from NOT_STARTED to IN_PROGRESS as soon as it has real work planned
        if (goal.getStatus() == GoalStatus.NOT_STARTED) {
            goal.setStatus(GoalStatus.IN_PROGRESS);
            goalRepository.save(goal);
        }

        return MilestoneResponse.from(saved);
    }

    @Override
    public List<MilestoneResponse> getAll(Long userId, Long goalId) {
        getOwnedGoal(userId, goalId); // ownership check
        return milestoneRepository.findByGoalIdOrderByDisplayOrderAsc(goalId)
                .stream()
                .map(MilestoneResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MilestoneResponse update(Long userId, Long goalId, Long milestoneId, MilestoneRequest request) {
        getOwnedGoal(userId, goalId);
        GoalMilestone milestone = milestoneRepository.findByIdAndGoalId(milestoneId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setTargetDate(request.getTargetDate());
        milestone.setDisplayOrder(request.getDisplayOrder());

        GoalMilestone saved = milestoneRepository.save(milestone);
        return MilestoneResponse.from(saved);
    }

    @Override
    @Transactional
    public MilestoneResponse toggleComplete(Long userId, Long goalId, Long milestoneId) {
        Goal goal = getOwnedGoal(userId, goalId);
        GoalMilestone milestone = milestoneRepository.findByIdAndGoalId(milestoneId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        boolean nowCompleted = !milestone.isCompleted();
        milestone.setCompleted(nowCompleted);
        milestone.setCompletedAt(nowCompleted ? LocalDateTime.now() : null);
        GoalMilestone saved = milestoneRepository.save(milestone);

        // Auto-complete the parent goal once every milestone is done
        long total = milestoneRepository.countByGoalId(goalId);
        long completed = milestoneRepository.countByGoalIdAndCompletedTrue(goalId);

        if (total > 0 && total == completed) {
            goal.setStatus(GoalStatus.COMPLETED);
            goalRepository.save(goal);
        } else if (goal.getStatus() == GoalStatus.COMPLETED) {
            // A milestone got un-checked after the goal was marked complete — reopen it
            goal.setStatus(GoalStatus.IN_PROGRESS);
            goalRepository.save(goal);
        }

        return MilestoneResponse.from(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long goalId, Long milestoneId) {
        getOwnedGoal(userId, goalId);
        GoalMilestone milestone = milestoneRepository.findByIdAndGoalId(milestoneId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));
        milestoneRepository.delete(milestone);
    }

    private Goal getOwnedGoal(Long userId, Long goalId) {
        return goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
    }
}