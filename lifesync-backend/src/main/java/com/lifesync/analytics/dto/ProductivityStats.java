package com.lifesync.analytics.dto;

public class ProductivityStats {

    private int tasksCompletedThisWeek;
    private int tasksPlannedThisWeek; // total tasks due this week, done or not
    private double completionRatePercentage;
    private int overdueTasksCount;

    public int getTasksCompletedThisWeek() {
        return tasksCompletedThisWeek;
    }

    public void setTasksCompletedThisWeek(int tasksCompletedThisWeek) {
        this.tasksCompletedThisWeek = tasksCompletedThisWeek;
    }

    public int getTasksPlannedThisWeek() {
        return tasksPlannedThisWeek;
    }

    public void setTasksPlannedThisWeek(int tasksPlannedThisWeek) {
        this.tasksPlannedThisWeek = tasksPlannedThisWeek;
    }

    public double getCompletionRatePercentage() {
        return completionRatePercentage;
    }

    public void setCompletionRatePercentage(double completionRatePercentage) {
        this.completionRatePercentage = completionRatePercentage;
    }

    public int getOverdueTasksCount() {
        return overdueTasksCount;
    }

    public void setOverdueTasksCount(int overdueTasksCount) {
        this.overdueTasksCount = overdueTasksCount;
    }
}