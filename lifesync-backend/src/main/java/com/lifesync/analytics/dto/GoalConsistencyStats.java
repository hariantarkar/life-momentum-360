package com.lifesync.analytics.dto;

public class GoalConsistencyStats {

    private int totalGoals;
    private int onTrackCount;
    private int atRiskCount;
    private int overdueCount;
    private int completedCount;
    private double averageProgressPercentage;

    public int getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }

    public int getOnTrackCount() {
        return onTrackCount;
    }

    public void setOnTrackCount(int onTrackCount) {
        this.onTrackCount = onTrackCount;
    }

    public int getAtRiskCount() {
        return atRiskCount;
    }

    public void setAtRiskCount(int atRiskCount) {
        this.atRiskCount = atRiskCount;
    }

    public int getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(int overdueCount) {
        this.overdueCount = overdueCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public double getAverageProgressPercentage() {
        return averageProgressPercentage;
    }

    public void setAverageProgressPercentage(double averageProgressPercentage) {
        this.averageProgressPercentage = averageProgressPercentage;
    }
}