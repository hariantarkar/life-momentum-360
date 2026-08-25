package com.lifesync.finance.service;

import com.lifesync.common.exception.BadRequestException;
import com.lifesync.finance.dto.BudgetRequest;
import com.lifesync.finance.dto.BudgetResponse;
import com.lifesync.finance.entity.Budget;
import com.lifesync.finance.repository.BudgetRepository;
import com.lifesync.finance.repository.ExpenseRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests the budget alert threshold boundary and the duplicate-budget guard —
 * the two rules with real business logic in this service, as opposed to plain CRUD.
 */
@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget buildBudget(Long id, String category, BigDecimal limit, YearMonth month, int threshold) {
        Budget budget = new Budget();
        budget.setId(id);
        budget.setUser(new User());
        budget.getUser().setId(100L);
        budget.setCategory(category);
        budget.setMonthlyLimit(limit);
        budget.setBudgetMonth(month);
        budget.setAlertThresholdPercentage(threshold);
        return budget;
    }

    @Test
    void create_blocksDuplicateCategoryAndMonth() {
        BudgetRequest request = new BudgetRequest();
        request.setCategory("Groceries");
        request.setMonthlyLimit(BigDecimal.valueOf(400));
        request.setBudgetMonth(YearMonth.of(2026, 8));

        when(budgetRepository.existsByUserIdAndCategoryAndBudgetMonth(100L, "Groceries", YearMonth.of(2026, 8)))
                .thenReturn(true);

        assertThatThrownBy(() -> budgetService.create(100L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void alertTriggered_exactlyAtThreshold_isInclusive() {
        // Spend exactly matches the 80% threshold — boundary should trigger the alert (>=, not >)
        Budget budget = buildBudget(1L, "Rent", BigDecimal.valueOf(1000), YearMonth.of(2026, 8), 80);

        when(budgetRepository.findByIdAndUserId(1L, 100L)).thenReturn(Optional.of(budget));
        when(expenseRepository.sumByUserAndCategoryAndDateRange(eq(100L), eq("Rent"), any(), any()))
                .thenReturn(BigDecimal.valueOf(800)); // exactly 80% of 1000

        BudgetResponse response = budgetService.getById(100L, 1L);

        assertThat(response.getSpentPercentage()).isEqualTo(80.0);
        assertThat(response.isAlertTriggered()).isTrue();
    }

    @Test
    void alertNotTriggered_belowThreshold() {
        Budget budget = buildBudget(2L, "Entertainment", BigDecimal.valueOf(200), YearMonth.of(2026, 8), 80);

        when(budgetRepository.findByIdAndUserId(2L, 100L)).thenReturn(Optional.of(budget));
        when(expenseRepository.sumByUserAndCategoryAndDateRange(eq(100L), eq("Entertainment"), any(), any()))
                .thenReturn(BigDecimal.valueOf(50)); // 25% of 200

        BudgetResponse response = budgetService.getById(100L, 2L);

        assertThat(response.getSpentPercentage()).isEqualTo(25.0);
        assertThat(response.isAlertTriggered()).isFalse();
        assertThat(response.getRemainingAmount()).isEqualTo(BigDecimal.valueOf(150));
    }

    @Test
    void customThreshold_respectsConfiguredValueInsteadOfDefault80() {
        // 60% spend, but this budget's custom threshold is 50% — should still trigger
        Budget budget = buildBudget(3L, "Shopping", BigDecimal.valueOf(200), YearMonth.of(2026, 8), 50);

        when(budgetRepository.findByIdAndUserId(3L, 100L)).thenReturn(Optional.of(budget));
        when(expenseRepository.sumByUserAndCategoryAndDateRange(eq(100L), eq("Shopping"), any(), any()))
                .thenReturn(BigDecimal.valueOf(120)); // 60% of 200

        BudgetResponse response = budgetService.getById(100L, 3L);

        assertThat(response.getSpentPercentage()).isEqualTo(60.0);
        assertThat(response.isAlertTriggered()).isTrue();
    }

    @Test
    void spendExceedsLimit_percentageCanGoOverOneHundred() {
        Budget budget = buildBudget(4L, "Travel", BigDecimal.valueOf(500), YearMonth.of(2026, 8), 80);

        when(budgetRepository.findByIdAndUserId(4L, 100L)).thenReturn(Optional.of(budget));
        when(expenseRepository.sumByUserAndCategoryAndDateRange(eq(100L), eq("Travel"), any(), any()))
                .thenReturn(BigDecimal.valueOf(600)); // over budget

        BudgetResponse response = budgetService.getById(100L, 4L);

        assertThat(response.getSpentPercentage()).isEqualTo(120.0);
        assertThat(response.getRemainingAmount()).isEqualTo(BigDecimal.valueOf(-100));
        assertThat(response.isAlertTriggered()).isTrue();
    }
}