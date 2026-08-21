package com.lifesync.learning.service;

import com.lifesync.learning.dto.LearningPathRequest;
import com.lifesync.learning.dto.LearningPathResponse;

import java.util.List;

public interface LearningPathService {
    LearningPathResponse create(Long userId, LearningPathRequest request);
    List<LearningPathResponse> getAll(Long userId);
    LearningPathResponse getById(Long userId, Long pathId);
    LearningPathResponse update(Long userId, Long pathId, LearningPathRequest request);
    void delete(Long userId, Long pathId);
    LearningPathResponse markComplete(Long userId, Long pathId);
}