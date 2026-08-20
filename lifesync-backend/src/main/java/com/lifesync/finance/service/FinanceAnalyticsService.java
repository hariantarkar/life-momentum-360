package com.lifesync.finance.service;

import com.lifesync.finance.dto.FinanceAnalyticsResponse;

import java.time.YearMonth;

public interface FinanceAnalyticsService {
    FinanceAnalyticsResponse getMonthlyAnalytics(Long userId, YearMonth month);
}