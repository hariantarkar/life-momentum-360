package com.lifesync.finance.repository;

import com.lifesync.finance.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserIdOrderByBudgetMonthDesc(Long userId);

    List<Budget> findByUserIdAndBudgetMonthOrderByCategoryAsc(Long userId, YearMonth budgetMonth);

    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndCategoryAndBudgetMonth(Long userId, String category, YearMonth budgetMonth);
}