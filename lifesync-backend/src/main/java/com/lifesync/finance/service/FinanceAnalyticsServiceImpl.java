package com.lifesync.finance.service;

import com.lifesync.finance.dto.CategorySpend;
import com.lifesync.finance.dto.FinanceAnalyticsResponse;
import com.lifesync.finance.repository.ExpenseRepository;
import com.lifesync.finance.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceAnalyticsServiceImpl implements FinanceAnalyticsService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public FinanceAnalyticsResponse getMonthlyAnalytics(Long userId, YearMonth month) {

        BigDecimal totalIncome = incomeRepository.sumByUserAndDateRange(userId, month.atDay(1), month.atEndOfMonth());
        BigDecimal totalExpense = expenseRepository.sumByUserAndDateRange(userId, month.atDay(1), month.atEndOfMonth());
        BigDecimal netSavings = totalIncome.subtract(totalExpense);

        List<Object[]> grouped = expenseRepository.sumGroupedByCategory(userId, month.atDay(1), month.atEndOfMonth());

        List<CategorySpend> breakdown = new ArrayList<>();
        for (Object[] row : grouped) {
            String category = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            double percentage = totalExpense.compareTo(BigDecimal.ZERO) == 0
                    ? 0.0
                    : amount.divide(totalExpense, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();
            breakdown.add(new CategorySpend(category, amount, percentage));
        }
        breakdown.sort((a, b) -> b.getAmount().compareTo(a.getAmount())); // highest spend first

        FinanceAnalyticsResponse response = new FinanceAnalyticsResponse();
        response.setMonth(month);
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetSavings(netSavings);
        response.setCategoryBreakdown(breakdown);
        return response;
    }
}