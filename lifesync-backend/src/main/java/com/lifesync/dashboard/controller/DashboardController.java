package com.lifesync.dashboard.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.dashboard.dto.DashboardResponse;
import com.lifesync.dashboard.service.DashboardService;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal UserPrincipal principal) {

        DashboardResponse dashboard = dashboardService.getDashboard(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }
}

