package com.lifesync.learning.dto;

import com.lifesync.learning.entity.SkillLevel;
import jakarta.validation.constraints.NotBlank;

public class SkillRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private SkillLevel currentLevel = SkillLevel.BEGINNER;
    private SkillLevel targetLevel; // optional
    private Long goalId; // optional

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkillLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(SkillLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public SkillLevel getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(SkillLevel targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}

