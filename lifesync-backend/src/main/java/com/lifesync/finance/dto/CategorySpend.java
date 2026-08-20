package com.lifesync.finance.dto;

import java.math.BigDecimal;

public class CategorySpend {

    private String category;
    private BigDecimal amount;
    private double percentageOfTotalExpense;

    public CategorySpend() {
    }

    public CategorySpend(String category, BigDecimal amount, double percentageOfTotalExpense) {
        this.category = category;
        this.amount = amount;
        this.percentageOfTotalExpense = percentageOfTotalExpense;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public double getPercentageOfTotalExpense() {
        return percentageOfTotalExpense;
    }
}