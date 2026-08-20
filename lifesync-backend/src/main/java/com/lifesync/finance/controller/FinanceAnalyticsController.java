package com.lifesync.finance.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.finance.dto.FinanceAnalyticsResponse;
import com.lifesync.finance.service.FinanceAnalyticsService;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/finance/analytics")
public class FinanceAnalyticsController {

    @Autowired
    private FinanceAnalyticsService financeAnalyticsService;

    /** ?month=2026-08 — defaults to the current month if omitted. */
    @GetMapping
    public ResponseEntity<ApiResponse<FinanceAnalyticsResponse>> getMonthlyAnalytics(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        YearMonth target = (month != null) ? month : YearMonth.now();
        return ResponseEntity.ok(ApiResponse.success(
                financeAnalyticsService.getMonthlyAnalytics(principal.getId(), target)));
    }
}