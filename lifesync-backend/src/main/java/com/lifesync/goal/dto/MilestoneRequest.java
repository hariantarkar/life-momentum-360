package com.lifesync.goal.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class MilestoneRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private LocalDate targetDate; // optional

    private int displayOrder;

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

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}