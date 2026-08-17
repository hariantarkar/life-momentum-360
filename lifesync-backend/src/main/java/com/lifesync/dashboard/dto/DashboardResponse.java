package com.lifesync.dashboard.dto;

import com.lifesync.lifearea.dto.LifeAreaResponse;

import java.util.List;

/**
 * Life Dashboard — the personal command center.
 * Right now this only wires up the user + life areas (Stage 2).
 * Later stages populate the placeholder fields below as each module is built:
 *   Stage 3-4: activeGoalsCount, tasksDueToday
 *   Stage 5:   habitsPendingToday
 *   Stage 7:   monthlyBudgetStatus
 *   Stage 10:  insights
 */
public class DashboardResponse {

    private String fullName;
    private String email;

    private int totalLifeAreas;
    private List<LifeAreaResponse> lifeAreas;

    // Placeholders — wired up in later stages, always 0/empty until then
    private int activeGoalsCount = 0;
    private int tasksDueToday = 0;
    private int habitsPendingToday = 0;

    private String note = "Goal, task, and habit widgets activate in Stages 3-5.";

    public DashboardResponse() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalLifeAreas() {
        return totalLifeAreas;
    }

    public void setTotalLifeAreas(int totalLifeAreas) {
        this.totalLifeAreas = totalLifeAreas;
    }

    public List<LifeAreaResponse> getLifeAreas() {
        return lifeAreas;
    }

    public void setLifeAreas(List<LifeAreaResponse> lifeAreas) {
        this.lifeAreas = lifeAreas;
    }

    public int getActiveGoalsCount() {
        return activeGoalsCount;
    }

    public int getTasksDueToday() {
        return tasksDueToday;
    }

    public int getHabitsPendingToday() {
        return habitsPendingToday;
    }

    public String getNote() {
        return note;
    }
}