package com.lifesync.analytics.dto;

import java.math.BigDecimal;

public class FinanceSnapshot {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;
    private Double savingsRatePercentage; // null if no income recorded

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(BigDecimal netSavings) {
        this.netSavings = netSavings;
    }

    public Double getSavingsRatePercentage() {
        return savingsRatePercentage;
    }

    public void setSavingsRatePercentage(Double savingsRatePercentage) {
        this.savingsRatePercentage = savingsRatePercentage;
    }
}