package com.lifesync.learning.dto;

import com.lifesync.learning.entity.SkillLevel;

import java.time.LocalDateTime;

public class SkillResponse {

    private Long id;
    private String name;
    private SkillLevel currentLevel;
    private SkillLevel targetLevel;
    private Integer levelGap; // null if no target set; otherwise targetLevel.ordinal() - currentLevel.ordinal()
    private Long goalId;
    private String goalTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getLevelGap() {
        return levelGap;
    }

    public void setLevelGap(Integer levelGap) {
        this.levelGap = levelGap;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}