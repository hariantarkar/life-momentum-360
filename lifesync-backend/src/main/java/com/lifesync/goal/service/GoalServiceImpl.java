package com.lifesync.goal.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.dto.GoalRequest;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.dto.MilestoneResponse;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.entity.GoalStatus;
import com.lifesync.goal.repository.GoalMilestoneRepository;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.lifearea.entity.LifeArea;
import com.lifesync.lifearea.repository.LifeAreaRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalMilestoneRepository milestoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LifeAreaRepository lifeAreaRepository;

    @Autowired
    private GoalHealthCalculator healthCalculator;

    @Override
    @Transactional
    public GoalResponse create(Long userId, GoalRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = new Goal();
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setTargetDate(request.getTargetDate());
        goal.setUser(user);
        goal.setStatus(GoalStatus.NOT_STARTED);

        if (request.getLifeAreaId() != null) {
            LifeArea lifeArea = lifeAreaRepository.findByIdAndUserId(request.getLifeAreaId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Life area not found"));
            goal.setLifeArea(lifeArea);
        }

        Goal saved = goalRepository.save(goal);
        return buildResponse(saved, false);
    }

    @Override
    public List<GoalResponse> getAll(Long userId) {
        return goalRepository.findByUserIdOrderByTargetDateAsc(userId)
                .stream()
                .map(goal -> buildResponse(goal, false)) // list view: skip milestone detail for a lighter payload
                .collect(Collectors.toList());
    }

    @Override
    public GoalResponse getById(Long userId, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        return buildResponse(goal, true); // detail view: include full milestone list
    }

    @Override
    @Transactional
    public GoalResponse update(Long userId, Long goalId, GoalRequest request) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setTargetDate(request.getTargetDate());

        if (request.getLifeAreaId() != null) {
            LifeArea lifeArea = lifeAreaRepository.findByIdAndUserId(request.getLifeAreaId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Life area not found"));
            goal.setLifeArea(lifeArea);
        } else {
            goal.setLifeArea(null);
        }

        Goal saved = goalRepository.save(goal);
        return buildResponse(saved, false);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        milestoneRepository.deleteByGoalId(goal.getId()); // clean up children first, no cascade mapping used
        goalRepository.delete(goal);
    }

    @Override
    @Transactional
    public GoalResponse markComplete(Long userId, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goal.setStatus(GoalStatus.COMPLETED);
        Goal saved = goalRepository.save(goal);
        return buildResponse(saved, false);
    }

    /**
     * Shared response builder — computes progress % and health on every read so they're
     * always fresh, never stored/stale values.
     */
    private GoalResponse buildResponse(Goal goal, boolean includeMilestones) {

        long total = milestoneRepository.countByGoalId(goal.getId());
        long completed = milestoneRepository.countByGoalIdAndCompletedTrue(goal.getId());
        double progress = healthCalculator.calculateProgress(total, completed);

        GoalResponse dto = new GoalResponse();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setTargetDate(goal.getTargetDate());
        dto.setStatus(goal.getStatus());
        dto.setProgressPercentage(progress);
        dto.setTotalMilestones((int) total);
        dto.setCompletedMilestones((int) completed);
        dto.setHealth(healthCalculator.calculate(goal.getStatus(), goal.getTargetDate(), progress));
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());

        if (goal.getLifeArea() != null) {
            dto.setLifeAreaId(goal.getLifeArea().getId());
            dto.setLifeAreaName(goal.getLifeArea().getName());
        }

        if (includeMilestones) {
            List<MilestoneResponse> milestones = milestoneRepository
                    .findByGoalIdOrderByDisplayOrderAsc(goal.getId())
                    .stream()
                    .map(MilestoneResponse::from)
                    .collect(Collectors.toList());
            dto.setMilestones(milestones);
        }

        return dto;
    }
}