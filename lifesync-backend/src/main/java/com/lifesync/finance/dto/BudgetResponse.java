package com.lifesync.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class BudgetResponse {

    private Long id;
    private String category;
    private BigDecimal monthlyLimit;
    private YearMonth budgetMonth;
    private int alertThresholdPercentage;
    private BigDecimal totalSpent;
    private BigDecimal remainingAmount;
    private double spentPercentage;
    private boolean alertTriggered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getAlertThresholdPercentage() {
        return alertThresholdPercentage;
    }

    public void setAlertThresholdPercentage(int alertThresholdPercentage) {
        this.alertThresholdPercentage = alertThresholdPercentage;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public double getSpentPercentage() {
        return spentPercentage;
    }

    public void setSpentPercentage(double spentPercentage) {
        this.spentPercentage = spentPercentage;
    }

    public boolean isAlertTriggered() {
        return alertTriggered;
    }

    public void setAlertTriggered(boolean alertTriggered) {
        this.alertTriggered = alertTriggered;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}