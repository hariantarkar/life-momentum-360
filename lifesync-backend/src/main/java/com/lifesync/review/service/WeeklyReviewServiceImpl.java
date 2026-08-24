package com.lifesync.review.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.finance.repository.ExpenseRepository;
import com.lifesync.finance.repository.IncomeRepository;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.service.GoalService;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.service.HabitService;
import com.lifesync.review.dto.WeeklyReviewResponse;
import com.lifesync.review.entity.WeeklyReview;
import com.lifesync.review.repository.WeeklyReviewRepository;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.entity.TaskStatus;
import com.lifesync.task.service.TaskService;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyReviewServiceImpl implements WeeklyReviewService {

    @Autowired
    private WeeklyReviewRepository weeklyReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public WeeklyReviewResponse generate(Long userId, LocalDate weekStartDate) {

        LocalDate weekStart = (weekStartDate != null) ? weekStartDate : LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // --- Tasks ---
        List<TaskResponse> allTasks = taskService.getAll(userId);
        int tasksCompleted = (int) allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE && t.getCompletedAt() != null)
                .filter(t -> {
                    LocalDate d = t.getCompletedAt().toLocalDate();
                    return !d.isBefore(weekStart) && !d.isAfter(weekEnd);
                })
                .count();
        int tasksOverdue = taskService.getOverdue(userId).size();

        // --- Goals ---
        List<GoalResponse> allGoals = goalService.getAll(userId);
        int goalsOnTrack = (int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.ON_TRACK).count();
        int goalsAtRisk = (int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.AT_RISK).count();
        int goalsOverdueCount = (int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.OVERDUE).count();
        int goalsCompleted = (int) allGoals.stream().filter(g -> g.getHealth() == GoalHealth.COMPLETED).count();

        // --- Habits ---
        List<HabitResponse> activeHabits = habitService.getAll(userId);
        double avgHabitAdherence = activeHabits.isEmpty() ? 0.0
                : Math.round(activeHabits.stream().mapToDouble(HabitResponse::getAdherencePercentage).average().orElse(0.0) * 100.0) / 100.0;

        // --- Finance (week-scoped, reusing Stage 7 repositories directly) ---
        BigDecimal totalIncome = incomeRepository.sumByUserAndDateRange(userId, weekStart, weekEnd);
        BigDecimal totalExpense = expenseRepository.sumByUserAndDateRange(userId, weekStart, weekEnd);
        BigDecimal netSavings = totalIncome.subtract(totalExpense);

        String summary = buildSummary(weekStart, weekEnd, tasksCompleted, tasksOverdue,
                goalsOnTrack, goalsAtRisk, goalsOverdueCount, goalsCompleted,
                avgHabitAdherence, totalIncome, totalExpense, netSavings);

        // Upsert: regenerating for a week that already has a review overwrites it
        WeeklyReview review = weeklyReviewRepository.findByUserIdAndWeekStartDate(userId, weekStart)
                .orElseGet(WeeklyReview::new);

        review.setUser(user);
        review.setWeekStartDate(weekStart);
        review.setWeekEndDate(weekEnd);
        review.setTasksCompleted(tasksCompleted);
        review.setTasksOverdue(tasksOverdue);
        review.setGoalsOnTrack(goalsOnTrack);
        review.setGoalsAtRisk(goalsAtRisk);
        review.setGoalsOverdueCount(goalsOverdueCount);
        review.setGoalsCompleted(goalsCompleted);
        review.setAvgHabitAdherence(avgHabitAdherence);
        review.setTotalIncome(totalIncome);
        review.setTotalExpense(totalExpense);
        review.setNetSavings(netSavings);
        review.setSummary(summary);

        WeeklyReview saved = weeklyReviewRepository.save(review);
        return toResponse(saved);
    }

    @Override
    public List<WeeklyReviewResponse> getAll(Long userId) {
        return weeklyReviewRepository.findByUserIdOrderByWeekStartDateDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public WeeklyReviewResponse getById(Long userId, Long reviewId) {
        WeeklyReview review = weeklyReviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly review not found"));
        return toResponse(review);
    }

    private String buildSummary(LocalDate weekStart, LocalDate weekEnd, int tasksCompleted, int tasksOverdue,
                                 int goalsOnTrack, int goalsAtRisk, int goalsOverdueCount, int goalsCompleted,
                                 double avgHabitAdherence, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal netSavings) {

        StringBuilder sb = new StringBuilder();
        sb.append("Week of ").append(weekStart).append(" to ").append(weekEnd).append(": ");
        sb.append("You completed ").append(tasksCompleted).append(" task(s)");
        if (tasksOverdue > 0) {
            sb.append(", with ").append(tasksOverdue).append(" currently overdue");
        }
        sb.append(". Goals: ").append(goalsOnTrack).append(" on track, ")
                .append(goalsAtRisk).append(" at risk, ")
                .append(goalsOverdueCount).append(" overdue, ")
                .append(goalsCompleted).append(" completed. ");
        sb.append("Average habit adherence was ").append(avgHabitAdherence).append("%. ");

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0 || totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("Finances: earned ").append(totalIncome).append(", spent ").append(totalExpense)
                    .append(", net ").append(netSavings.compareTo(BigDecimal.ZERO) >= 0 ? "savings of " : "deficit of ")
                    .append(netSavings.abs()).append(".");
        }

        return sb.toString();
    }

    private WeeklyReviewResponse toResponse(WeeklyReview review) {
        WeeklyReviewResponse dto = new WeeklyReviewResponse();
        dto.setId(review.getId());
        dto.setWeekStartDate(review.getWeekStartDate());
        dto.setWeekEndDate(review.getWeekEndDate());
        dto.setTasksCompleted(review.getTasksCompleted());
        dto.setTasksOverdue(review.getTasksOverdue());
        dto.setGoalsOnTrack(review.getGoalsOnTrack());
        dto.setGoalsAtRisk(review.getGoalsAtRisk());
        dto.setGoalsOverdueCount(review.getGoalsOverdueCount());
        dto.setGoalsCompleted(review.getGoalsCompleted());
        dto.setAvgHabitAdherence(review.getAvgHabitAdherence());
        dto.setTotalIncome(review.getTotalIncome());
        dto.setTotalExpense(review.getTotalExpense());
        dto.setNetSavings(review.getNetSavings());
        dto.setSummary(review.getSummary());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}