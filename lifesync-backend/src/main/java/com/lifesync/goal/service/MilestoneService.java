package com.lifesync.goal.service;

import com.lifesync.goal.dto.MilestoneRequest;
import com.lifesync.goal.dto.MilestoneResponse;

import java.util.List;

public interface MilestoneService {
    MilestoneResponse create(Long userId, Long goalId, MilestoneRequest request);
    List<MilestoneResponse> getAll(Long userId, Long goalId);
    MilestoneResponse update(Long userId, Long goalId, Long milestoneId, MilestoneRequest request);
    MilestoneResponse toggleComplete(Long userId, Long goalId, Long milestoneId);
    void delete(Long userId, Long goalId, Long milestoneId);
}