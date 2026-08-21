package com.lifesync.learning.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.learning.dto.LearningPathRequest;
import com.lifesync.learning.dto.LearningPathResponse;
import com.lifesync.learning.entity.LearningPath;
import com.lifesync.learning.entity.LearningPathStatus;
import com.lifesync.learning.entity.Skill;
import com.lifesync.learning.repository.LearningPathRepository;
import com.lifesync.learning.repository.LearningSessionRepository;
import com.lifesync.learning.repository.SkillRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LearningPathServiceImpl implements LearningPathService {

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private LearningSessionRepository learningSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    @Transactional
    public LearningPathResponse create(Long userId, LearningPathRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LearningPath path = new LearningPath();
        applyRequest(path, request, userId);
        path.setUser(user);
        path.setStatus(LearningPathStatus.NOT_STARTED);

        LearningPath saved = learningPathRepository.save(path);
        return toResponse(saved);
    }

    @Override
    public List<LearningPathResponse> getAll(Long userId) {
        return learningPathRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public LearningPathResponse getById(Long userId, Long pathId) {
        return toResponse(getOwned(userId, pathId));
    }

    @Override
    @Transactional
    public LearningPathResponse update(Long userId, Long pathId, LearningPathRequest request) {
        LearningPath path = getOwned(userId, pathId);
        applyRequest(path, request, userId);
        return toResponse(learningPathRepository.save(path));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long pathId) {
        LearningPath path = getOwned(userId, pathId);
        learningSessionRepository.deleteByLearningPathId(path.getId());
        learningPathRepository.delete(path);
    }

    @Override
    @Transactional
    public LearningPathResponse markComplete(Long userId, Long pathId) {
        LearningPath path = getOwned(userId, pathId);
        path.setStatus(LearningPathStatus.COMPLETED);
        return toResponse(learningPathRepository.save(path));
    }

    private LearningPath getOwned(Long userId, Long pathId) {
        return learningPathRepository.findByIdAndUserId(pathId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found"));
    }

    private void applyRequest(LearningPath path, LearningPathRequest request, Long userId) {
        path.setTitle(request.getTitle());
        path.setDescription(request.getDescription());
        path.setTargetCompletionDate(request.getTargetCompletionDate());

        if (request.getSkillId() != null) {
            Skill skill = skillRepository.findByIdAndUserId(request.getSkillId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
            path.setSkill(skill);
        } else {
            path.setSkill(null);
        }

        if (request.getGoalId() != null) {
            Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
            path.setGoal(goal);
        } else {
            path.setGoal(null);
        }
    }

    private LearningPathResponse toResponse(LearningPath path) {
        LearningPathResponse dto = new LearningPathResponse();
        dto.setId(path.getId());
        dto.setTitle(path.getTitle());
        dto.setDescription(path.getDescription());
        dto.setTargetCompletionDate(path.getTargetCompletionDate());
        dto.setStatus(path.getStatus());
        dto.setCreatedAt(path.getCreatedAt());
        dto.setUpdatedAt(path.getUpdatedAt());

        if (path.getSkill() != null) {
            dto.setSkillId(path.getSkill().getId());
            dto.setSkillName(path.getSkill().getName());
        }

        if (path.getGoal() != null) {
            dto.setGoalId(path.getGoal().getId());
            dto.setGoalTitle(path.getGoal().getTitle());
        }

        dto.setTotalSessions((int) learningSessionRepository.countByLearningPathId(path.getId()));
        dto.setTotalMinutes(learningSessionRepository.sumDurationByLearningPathId(path.getId()));

        return dto;
    }
}