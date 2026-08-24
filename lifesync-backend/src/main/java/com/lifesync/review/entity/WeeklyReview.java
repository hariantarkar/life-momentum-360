package com.lifesync.review.entity;

import com.lifesync.user.entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weekly_reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "week_start_date"}))
public class WeeklyReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate; // always a Monday

    @Column(name = "week_end_date", nullable = false)
    private LocalDate weekEndDate; // always the following Sunday

    private int tasksCompleted;
    private int tasksOverdue;

    private int goalsOnTrack;
    private int goalsAtRisk;
    private int goalsOverdueCount;
    private int goalsCompleted;

    private double avgHabitAdherence;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalIncome;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalExpense;

    @Column(precision = 12, scale = 2)
    private BigDecimal netSavings;

    @Column(length = 2000)
    private String summary;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WeeklyReview() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}