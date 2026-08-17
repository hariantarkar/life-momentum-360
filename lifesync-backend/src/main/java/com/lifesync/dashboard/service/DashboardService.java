package com.lifesync.dashboard.service;

import com.lifesync.dashboard.dto.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboard(Long userId);
}