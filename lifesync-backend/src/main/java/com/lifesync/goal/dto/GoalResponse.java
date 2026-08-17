package com.lifesync.goal.dto;

import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.entity.GoalStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoalResponse {

    private Long id;
    private String title;
    private String description;
    private Long lifeAreaId;
    private String lifeAreaName;
    private LocalDate targetDate;
    private GoalStatus status;
    private GoalHealth health;
    private double progressPercentage;
    private int totalMilestones;
    private int completedMilestones;
    private List<MilestoneResponse> milestones; // included on getById, omitted on list views
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GoalResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getLifeAreaId() {
        return lifeAreaId;
    }

    public void setLifeAreaId(Long lifeAreaId) {
        this.lifeAreaId = lifeAreaId;
    }

    public String getLifeAreaName() {
        return lifeAreaName;
    }

    public void setLifeAreaName(String lifeAreaName) {
        this.lifeAreaName = lifeAreaName;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public GoalHealth getHealth() {
        return health;
    }

    public void setHealth(GoalHealth health) {
        this.health = health;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public int getTotalMilestones() {
        return totalMilestones;
    }

    public void setTotalMilestones(int totalMilestones) {
        this.totalMilestones = totalMilestones;
    }

    public int getCompletedMilestones() {
        return completedMilestones;
    }

    public void setCompletedMilestones(int completedMilestones) {
        this.completedMilestones = completedMilestones;
    }

    public List<MilestoneResponse> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneResponse> milestones) {
        this.milestones = milestones;
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