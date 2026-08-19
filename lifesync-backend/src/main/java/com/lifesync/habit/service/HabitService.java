package com.lifesync.habit.service;

import com.lifesync.habit.dto.HabitLogResponse;
import com.lifesync.habit.dto.HabitRequest;
import com.lifesync.habit.dto.HabitResponse;

import java.util.List;

public interface HabitService {
    HabitResponse create(Long userId, HabitRequest request);
    List<HabitResponse> getAll(Long userId);
    HabitResponse getById(Long userId, Long habitId);
    HabitResponse update(Long userId, Long habitId, HabitRequest request);
    void delete(Long userId, Long habitId);
    HabitResponse logToday(Long userId, Long habitId);
    HabitResponse unlogToday(Long userId, Long habitId);
    List<HabitLogResponse> getLogs(Long userId, Long habitId);
}