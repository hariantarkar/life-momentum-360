package com.lifesync.analytics.controller;

import com.lifesync.analytics.dto.LifeAnalyticsResponse;
import com.lifesync.analytics.service.AnalyticsService;
import com.lifesync.common.response.ApiResponse;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /** The full Life Analytics dashboard: productivity, goal consistency, habit consistency, finance snapshot, and insights. */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<LifeAnalyticsResponse>> getOverview(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(analyticsService.getOverview(principal.getId())));
    }
}