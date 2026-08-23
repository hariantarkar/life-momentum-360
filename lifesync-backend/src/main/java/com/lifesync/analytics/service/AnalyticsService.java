package com.lifesync.analytics.service;

import com.lifesync.analytics.dto.LifeAnalyticsResponse;

public interface AnalyticsService {
    LifeAnalyticsResponse getOverview(Long userId);
}