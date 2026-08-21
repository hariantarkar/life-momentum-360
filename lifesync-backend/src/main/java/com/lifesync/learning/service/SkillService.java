package com.lifesync.learning.service;

import com.lifesync.learning.dto.SkillRequest;
import com.lifesync.learning.dto.SkillResponse;

import java.util.List;

public interface SkillService {
    SkillResponse create(Long userId, SkillRequest request);
    List<SkillResponse> getAll(Long userId);
    SkillResponse getById(Long userId, Long skillId);
    SkillResponse update(Long userId, Long skillId, SkillRequest request);
    void delete(Long userId, Long skillId);
}