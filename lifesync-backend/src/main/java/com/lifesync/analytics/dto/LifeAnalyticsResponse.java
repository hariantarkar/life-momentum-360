package com.lifesync.analytics.dto;

import java.time.LocalDate;
import java.util.List;

public class LifeAnalyticsResponse {

    private LocalDate generatedOn;
    private ProductivityStats productivity;
    private GoalConsistencyStats goals;
    private HabitConsistencyStats habits;
    private FinanceSnapshot finance;
    private List<Insight> insights;

    public LocalDate getGeneratedOn() {
        return generatedOn;
    }

    public void setGeneratedOn(LocalDate generatedOn) {
        this.generatedOn = generatedOn;
    }

    public ProductivityStats getProductivity() {
        return productivity;
    }

    public void setProductivity(ProductivityStats productivity) {
        this.productivity = productivity;
    }

    public GoalConsistencyStats getGoals() {
        return goals;
    }

    public void setGoals(GoalConsistencyStats goals) {
        this.goals = goals;
    }

    public HabitConsistencyStats getHabits() {
        return habits;
    }

    public void setHabits(HabitConsistencyStats habits) {
        this.habits = habits;
    }

    public FinanceSnapshot getFinance() {
        return finance;
    }

    public void setFinance(FinanceSnapshot finance) {
        this.finance = finance;
    }

    public List<Insight> getInsights() {
        return insights;
    }

    public void setInsights(List<Insight> insights) {
        this.insights = insights;
    }
}