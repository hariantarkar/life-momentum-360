package com.lifesync.learning.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.learning.dto.SkillRequest;
import com.lifesync.learning.dto.SkillResponse;
import com.lifesync.learning.entity.Skill;
import com.lifesync.learning.repository.SkillRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Override
    @Transactional
    public SkillResponse create(Long userId, SkillRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Skill skill = new Skill();
        applyRequest(skill, request, userId);
        skill.setUser(user);

        Skill saved = skillRepository.save(skill);
        return toResponse(saved);
    }

    @Override
    public List<SkillResponse> getAll(Long userId) {
        return skillRepository.findByUserIdOrderByNameAsc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SkillResponse getById(Long userId, Long skillId) {
        return toResponse(getOwned(userId, skillId));
    }

    @Override
    @Transactional
    public SkillResponse update(Long userId, Long skillId, SkillRequest request) {
        Skill skill = getOwned(userId, skillId);
        applyRequest(skill, request, userId);
        return toResponse(skillRepository.save(skill));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long skillId) {
        skillRepository.delete(getOwned(userId, skillId));
    }

    private Skill getOwned(Long userId, Long skillId) {
        return skillRepository.findByIdAndUserId(skillId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    }

    private void applyRequest(Skill skill, SkillRequest request, Long userId) {
        skill.setName(request.getName());
        skill.setCurrentLevel(request.getCurrentLevel());
        skill.setTargetLevel(request.getTargetLevel());

        if (request.getGoalId() != null) {
            Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
            skill.setGoal(goal);
        } else {
            skill.setGoal(null);
        }
    }

    private SkillResponse toResponse(Skill skill) {
        SkillResponse dto = new SkillResponse();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setCurrentLevel(skill.getCurrentLevel());
        dto.setTargetLevel(skill.getTargetLevel());
        dto.setCreatedAt(skill.getCreatedAt());
        dto.setUpdatedAt(skill.getUpdatedAt());

        if (skill.getTargetLevel() != null) {
            dto.setLevelGap(skill.getTargetLevel().ordinal() - skill.getCurrentLevel().ordinal());
        }

        if (skill.getGoal() != null) {
            dto.setGoalId(skill.getGoal().getId());
            dto.setGoalTitle(skill.getGoal().getTitle());
        }

        return dto;
    }
}