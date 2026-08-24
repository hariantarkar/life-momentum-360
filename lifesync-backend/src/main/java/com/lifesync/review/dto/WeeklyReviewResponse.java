package com.lifesync.review.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeeklyReviewResponse {

    private Long id;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private int tasksCompleted;
    private int tasksOverdue;
    private int goalsOnTrack;
    private int goalsAtRisk;
    private int goalsOverdueCount;
    private int goalsCompleted;
    private double avgHabitAdherence;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;
    private String summary;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getTasksOverdue() {
        return tasksOverdue;
    }

    public void setTasksOverdue(int tasksOverdue) {
        this.tasksOverdue = tasksOverdue;
    }

    public int getGoalsOnTrack() {
        return goalsOnTrack;
    }

    public void setGoalsOnTrack(int goalsOnTrack) {
        this.goalsOnTrack = goalsOnTrack;
    }

    public int getGoalsAtRisk() {
        return goalsAtRisk;
    }

    public void setGoalsAtRisk(int goalsAtRisk) {
        this.goalsAtRisk = goalsAtRisk;
    }

    public int getGoalsOverdueCount() {
        return goalsOverdueCount;
    }

    public void setGoalsOverdueCount(int goalsOverdueCount) {
        this.goalsOverdueCount = goalsOverdueCount;
    }

    public int getGoalsCompleted() {
        return goalsCompleted;
    }

    public void setGoalsCompleted(int goalsCompleted) {
        this.goalsCompleted = goalsCompleted;
    }

    public double getAvgHabitAdherence() {
        return avgHabitAdherence;
    }

    public void setAvgHabitAdherence(double avgHabitAdherence) {
        this.avgHabitAdherence = avgHabitAdherence;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}