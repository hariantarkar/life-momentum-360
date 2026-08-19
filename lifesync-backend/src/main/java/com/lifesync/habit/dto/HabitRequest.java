package com.lifesync.habit.dto;

import com.lifesync.habit.entity.HabitFrequency;
import jakarta.validation.constraints.NotBlank;

public class HabitRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Long goalId; // optional

    private HabitFrequency frequency = HabitFrequency.DAILY;

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

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }
}