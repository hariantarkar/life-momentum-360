package com.lifesync.finance.service;

import com.lifesync.finance.dto.IncomeRequest;
import com.lifesync.finance.dto.IncomeResponse;

import java.util.List;

public interface IncomeService {
    IncomeResponse create(Long userId, IncomeRequest request);
    List<IncomeResponse> getAll(Long userId);
    IncomeResponse getById(Long userId, Long incomeId);
    IncomeResponse update(Long userId, Long incomeId, IncomeRequest request);
    void delete(Long userId, Long incomeId);
}