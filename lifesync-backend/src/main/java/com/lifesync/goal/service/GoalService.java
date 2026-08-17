package com.lifesync.goal.service;

import com.lifesync.goal.dto.GoalRequest;
import com.lifesync.goal.dto.GoalResponse;

import java.util.List;

public interface GoalService {
    GoalResponse create(Long userId, GoalRequest request);
    List<GoalResponse> getAll(Long userId);
    GoalResponse getById(Long userId, Long goalId);
    GoalResponse update(Long userId, Long goalId, GoalRequest request);
    void delete(Long userId, Long goalId);
    GoalResponse markComplete(Long userId, Long goalId);
}