package com.lifesync.goal.dto;

import com.lifesync.goal.entity.GoalMilestone;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MilestoneResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate targetDate;
    private boolean completed;
    private LocalDateTime completedAt;
    private int displayOrder;

    public MilestoneResponse() {
    }

    public static MilestoneResponse from(GoalMilestone milestone) {
        MilestoneResponse dto = new MilestoneResponse();
        dto.id = milestone.getId();
        dto.title = milestone.getTitle();
        dto.description = milestone.getDescription();
        dto.targetDate = milestone.getTargetDate();
        dto.completed = milestone.isCompleted();
        dto.completedAt = milestone.getCompletedAt();
        dto.displayOrder = milestone.getDisplayOrder();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}