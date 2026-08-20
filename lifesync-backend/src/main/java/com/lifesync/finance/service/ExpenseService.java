package com.lifesync.finance.service;

import com.lifesync.finance.dto.ExpenseRequest;
import com.lifesync.finance.dto.ExpenseResponse;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse create(Long userId, ExpenseRequest request);
    List<ExpenseResponse> getAll(Long userId);
    ExpenseResponse getById(Long userId, Long expenseId);
    ExpenseResponse update(Long userId, Long expenseId, ExpenseRequest request);
    void delete(Long userId, Long expenseId);
}