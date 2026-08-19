package com.lifesync.habit.dto;

import com.lifesync.habit.entity.HabitFrequency;

import java.time.LocalDateTime;

public class HabitResponse {

    private Long id;
    private String title;
    private String description;
    private Long goalId;
    private String goalTitle;
    private HabitFrequency frequency;
    private boolean active;
    private int currentStreak;
    private double adherencePercentage;
    private boolean loggedForCurrentPeriod; // true if today (or this week) already has a log
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public double getAdherencePercentage() {
        return adherencePercentage;
    }

    public void setAdherencePercentage(double adherencePercentage) {
        this.adherencePercentage = adherencePercentage;
    }

    public boolean isLoggedForCurrentPeriod() {
        return loggedForCurrentPeriod;
    }

    public void setLoggedForCurrentPeriod(boolean loggedForCurrentPeriod) {
        this.loggedForCurrentPeriod = loggedForCurrentPeriod;
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