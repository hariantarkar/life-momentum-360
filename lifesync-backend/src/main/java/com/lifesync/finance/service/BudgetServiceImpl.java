package com.lifesync.finance.service;

import com.lifesync.common.exception.BadRequestException;
import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.finance.dto.BudgetRequest;
import com.lifesync.finance.dto.BudgetResponse;
import com.lifesync.finance.entity.Budget;
import com.lifesync.finance.repository.BudgetRepository;
import com.lifesync.finance.repository.ExpenseRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public BudgetResponse create(Long userId, BudgetRequest request) {

        if (budgetRepository.existsByUserIdAndCategoryAndBudgetMonth(userId, request.getCategory(), request.getBudgetMonth())) {
            throw new BadRequestException(
                    "A budget for \"" + request.getCategory() + "\" already exists for " + request.getBudgetMonth());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Budget budget = new Budget();
        applyRequest(budget, request);
        budget.setUser(user);

        Budget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public List<BudgetResponse> getAll(Long userId) {
        return budgetRepository.findByUserIdOrderByBudgetMonthDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BudgetResponse> getByMonth(Long userId, YearMonth month) {
        return budgetRepository.findByUserIdAndBudgetMonthOrderByCategoryAsc(userId, month)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public BudgetResponse getById(Long userId, Long budgetId) {
        return toResponse(getOwned(userId, budgetId));
    }

    @Override
    @Transactional
    public BudgetResponse update(Long userId, Long budgetId, BudgetRequest request) {
        Budget budget = getOwned(userId, budgetId);
        applyRequest(budget, request);
        return toResponse(budgetRepository.save(budget));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long budgetId) {
        budgetRepository.delete(getOwned(userId, budgetId));
    }

    private Budget getOwned(Long userId, Long budgetId) {
        return budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
    }

    private void applyRequest(Budget budget, BudgetRequest request) {
        budget.setCategory(request.getCategory());
        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setBudgetMonth(request.getBudgetMonth());
        budget.setAlertThresholdPercentage(
                request.getAlertThresholdPercentage() != null ? request.getAlertThresholdPercentage() : 80);
    }

    /**
     * Computes live spend against this budget by summing actual expenses in the same
     * category and month — never stored, always fresh, same pattern as Goal Health.
     */
    private BudgetResponse toResponse(Budget budget) {
        YearMonth month = budget.getBudgetMonth();
        BigDecimal spent = expenseRepository.sumByUserAndCategoryAndDateRange(
                budget.getUser().getId(), budget.getCategory(), month.atDay(1), month.atEndOfMonth());

        BigDecimal remaining = budget.getMonthlyLimit().subtract(spent);
        double spentPercentage = budget.getMonthlyLimit().compareTo(BigDecimal.ZERO) == 0
                ? 0.0
                : spent.divide(budget.getMonthlyLimit(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

        BudgetResponse dto = new BudgetResponse();
        dto.setId(budget.getId());
        dto.setCategory(budget.getCategory());
        dto.setMonthlyLimit(budget.getMonthlyLimit());
        dto.setBudgetMonth(budget.getBudgetMonth());
        dto.setAlertThresholdPercentage(budget.getAlertThresholdPercentage());
        dto.setTotalSpent(spent);
        dto.setRemainingAmount(remaining);
        dto.setSpentPercentage(spentPercentage);
        dto.setAlertTriggered(spentPercentage >= budget.getAlertThresholdPercentage());
        dto.setCreatedAt(budget.getCreatedAt());
        dto.setUpdatedAt(budget.getUpdatedAt());
        return dto;
    }
}