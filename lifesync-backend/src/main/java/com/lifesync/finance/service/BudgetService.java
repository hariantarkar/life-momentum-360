package com.lifesync.finance.service;

import com.lifesync.finance.dto.BudgetRequest;
import com.lifesync.finance.dto.BudgetResponse;

import java.time.YearMonth;
import java.util.List;

public interface BudgetService {
    BudgetResponse create(Long userId, BudgetRequest request);
    List<BudgetResponse> getAll(Long userId);
    List<BudgetResponse> getByMonth(Long userId, YearMonth month);
    BudgetResponse getById(Long userId, Long budgetId);
    BudgetResponse update(Long userId, Long budgetId, BudgetRequest request);
    void delete(Long userId, Long budgetId);
}