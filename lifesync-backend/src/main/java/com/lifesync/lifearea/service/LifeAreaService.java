package com.lifesync.lifearea.service;

import com.lifesync.lifearea.dto.LifeAreaRequest;
import com.lifesync.lifearea.dto.LifeAreaResponse;

import java.util.List;

public interface LifeAreaService {
    LifeAreaResponse create(Long userId, LifeAreaRequest request);
    List<LifeAreaResponse> getAll(Long userId);
    LifeAreaResponse getById(Long userId, Long id);
    LifeAreaResponse update(Long userId, Long id, LifeAreaRequest request);
    void delete(Long userId, Long id);
}