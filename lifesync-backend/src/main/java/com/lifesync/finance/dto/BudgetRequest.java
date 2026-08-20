package com.lifesync.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public class BudgetRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Monthly limit is required")
    @DecimalMin(value = "0.01", message = "Monthly limit must be greater than 0")
    private BigDecimal monthlyLimit;

    @NotNull(message = "Budget month is required (format: yyyy-MM)")
    private YearMonth budgetMonth;

    private Integer alertThresholdPercentage; // optional, defaults to 80 in service

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public YearMonth getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(YearMonth budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    public Integer getAlertThresholdPercentage() {
        return alertThresholdPercentage;
    }

    public void setAlertThresholdPercentage(Integer alertThresholdPercentage) {
        this.alertThresholdPercentage = alertThresholdPercentage;
    }
}