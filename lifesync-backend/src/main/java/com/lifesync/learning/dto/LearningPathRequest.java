package com.lifesync.learning.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class LearningPathRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Long skillId; // optional
    private Long goalId;  // optional
    private LocalDate targetCompletionDate; // optional

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public LocalDate getTargetCompletionDate() {
        return targetCompletionDate;
    }

    public void setTargetCompletionDate(LocalDate targetCompletionDate) {
        this.targetCompletionDate = targetCompletionDate;
    }
}