package com.lifesync.learning.service;

import com.lifesync.learning.dto.LearningSessionRequest;
import com.lifesync.learning.dto.LearningSessionResponse;

import java.util.List;

public interface LearningSessionService {
    LearningSessionResponse create(Long userId, Long pathId, LearningSessionRequest request);
    List<LearningSessionResponse> getAll(Long userId, Long pathId);
    void delete(Long userId, Long pathId, Long sessionId);
}