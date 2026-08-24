package com.lifesync.export.dto;

import com.lifesync.auth.dto.UserSummary;
import com.lifesync.document.dto.DocumentResponse;
import com.lifesync.finance.dto.BudgetResponse;
import com.lifesync.finance.dto.ExpenseResponse;
import com.lifesync.finance.dto.IncomeResponse;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.learning.dto.LearningPathResponse;
import com.lifesync.learning.dto.SkillResponse;
import com.lifesync.lifearea.dto.LifeAreaResponse;
import com.lifesync.task.dto.TaskResponse;

import java.time.LocalDateTime;
import java.util.List;

/** A full snapshot of everything the user owns — "your data, exportable on demand." */
public class UserDataExportResponse {

    private LocalDateTime exportedAt;
    private UserSummary profile;
    private List<LifeAreaResponse> lifeAreas;
    private List<GoalResponse> goals; // includes milestones (fetched via getById per goal)
    private List<TaskResponse> tasks;
    private List<HabitResponse> habits;
    private List<IncomeResponse> incomes;
    private List<ExpenseResponse> expenses;
    private List<BudgetResponse> budgets;
    private List<DocumentResponse> documents;
    private List<SkillResponse> skills;
    private List<LearningPathResponse> learningPaths;

    public LocalDateTime getExportedAt() {
        return exportedAt;
    }

    public void setExportedAt(LocalDateTime exportedAt) {
        this.exportedAt = exportedAt;
    }

    public UserSummary getProfile() {
        return profile;
    }

    public void setProfile(UserSummary profile) {
        this.profile = profile;
    }

    public List<LifeAreaResponse> getLifeAreas() {
        return lifeAreas;
    }

    public void setLifeAreas(List<LifeAreaResponse> lifeAreas) {
        this.lifeAreas = lifeAreas;
    }

    public List<GoalResponse> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalResponse> goals) {
        this.goals = goals;
    }

    public List<TaskResponse> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskResponse> tasks) {
        this.tasks = tasks;
    }

    public List<HabitResponse> getHabits() {
        return habits;
    }

    public void setHabits(List<HabitResponse> habits) {
        this.habits = habits;
    }

    public List<IncomeResponse> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<IncomeResponse> incomes) {
        this.incomes = incomes;
    }

    public List<ExpenseResponse> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponse> expenses) {
        this.expenses = expenses;
    }

    public List<BudgetResponse> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<BudgetResponse> budgets) {
        this.budgets = budgets;
    }

    public List<DocumentResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentResponse> documents) {
        this.documents = documents;
    }

    public List<SkillResponse> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillResponse> skills) {
        this.skills = skills;
    }

    public List<LearningPathResponse> getLearningPaths() {
        return learningPaths;
    }

    public void setLearningPaths(List<LearningPathResponse> learningPaths) {
        this.learningPaths = learningPaths;
    }
}