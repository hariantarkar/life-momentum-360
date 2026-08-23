package com.lifesync.analytics.dto;

public class HabitConsistencyStats {

    private int totalActiveHabits;
    private int habitsWithActiveStreak; // currentStreak > 0
    private double averageAdherencePercentage;

    public int getTotalActiveHabits() {
        return totalActiveHabits;
    }

    public void setTotalActiveHabits(int totalActiveHabits) {
        this.totalActiveHabits = totalActiveHabits;
    }

    public int getHabitsWithActiveStreak() {
        return habitsWithActiveStreak;
    }

    public void setHabitsWithActiveStreak(int habitsWithActiveStreak) {
        this.habitsWithActiveStreak = habitsWithActiveStreak;
    }

    public double getAverageAdherencePercentage() {
        return averageAdherencePercentage;
    }

    public void setAverageAdherencePercentage(double averageAdherencePercentage) {
        this.averageAdherencePercentage = averageAdherencePercentage;
    }
}