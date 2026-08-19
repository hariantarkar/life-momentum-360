package com.lifesync.habit.dto;

import com.lifesync.habit.entity.HabitLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HabitLogResponse {

    private Long id;
    private LocalDate logDate;
    private boolean completed;
    private LocalDateTime createdAt;

    public static HabitLogResponse from(HabitLog log) {
        HabitLogResponse dto = new HabitLogResponse();
        dto.id = log.getId();
        dto.logDate = log.getLogDate();
        dto.completed = log.isCompleted();
        dto.createdAt = log.getCreatedAt();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}