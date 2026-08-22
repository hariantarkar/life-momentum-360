package com.lifesync.notification.service;

import com.lifesync.finance.dto.BudgetResponse;
import com.lifesync.finance.service.BudgetService;
import com.lifesync.document.dto.DocumentResponse;
import com.lifesync.document.entity.DocumentStatus;
import com.lifesync.document.service.DocumentService;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.entity.GoalHealth;
import com.lifesync.goal.service.GoalService;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.entity.HabitFrequency;
import com.lifesync.habit.service.HabitService;
import com.lifesync.notification.entity.Notification;
import com.lifesync.notification.entity.NotificationType;
import com.lifesync.notification.repository.NotificationRepository;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.service.TaskService;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The automation core of the Reminder & Notification Center.
 *
 * Rather than re-deriving overdue/at-risk/expiring logic, this reuses the
 * already-computed response DTOs from every other module (Goal Health from
 * Stage 3, task overdue detection from Stage 4, habit streaks from Stage 5,
 * document expiry from Stage 8, budget alerts from Stage 7) and reacts to
 * their computed fields. One source of truth per rule, no duplication.
 */
@Service
public class NotificationGeneratorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private NotificationRepository notificationRepository;

    /** Runs the full scan for every registered user — used by the scheduler. */
    @Transactional
    public void generateForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            generateForUser(user.getId());
        }
    }

    /** Runs the full scan for a single user — used by the manual trigger endpoint and the batch job above. */
    @Transactional
    public int generateForUser(Long userId) {
        int created = 0;
        created += scanTasks(userId);
        created += scanGoals(userId);
        created += scanHabits(userId);
        created += scanDocuments(userId);
        created += scanBudgets(userId);
        return created;
    }

    private int scanTasks(Long userId) {
        int count = 0;
        for (TaskResponse task : taskService.getOverdue(userId)) {
            count += createIfNew(userId, NotificationType.TASK_OVERDUE, "TASK", task.getId(),
                    "Task overdue: " + task.getTitle(),
                    "\"" + task.getTitle() + "\" was due " + task.getDueDate() + " and is still not done.");
        }
        for (TaskResponse task : taskService.getDueToday(userId)) {
            count += createIfNew(userId, NotificationType.TASK_DUE_TODAY, "TASK", task.getId(),
                    "Task due today: " + task.getTitle(),
                    "\"" + task.getTitle() + "\" is due today.");
        }
        return count;
    }

    private int scanGoals(Long userId) {
        int count = 0;
        for (GoalResponse goal : goalService.getAll(userId)) {
            if (goal.getHealth() == GoalHealth.AT_RISK) {
                count += createIfNew(userId, NotificationType.GOAL_AT_RISK, "GOAL", goal.getId(),
                        "Goal at risk: " + goal.getTitle(),
                        "\"" + goal.getTitle() + "\" is " + goal.getProgressPercentage() + "% complete with its deadline approaching.");
            } else if (goal.getHealth() == GoalHealth.OVERDUE) {
                count += createIfNew(userId, NotificationType.GOAL_OVERDUE, "GOAL", goal.getId(),
                        "Goal overdue: " + goal.getTitle(),
                        "\"" + goal.getTitle() + "\" has passed its target date of " + goal.getTargetDate() + ".");
            }
        }
        return count;
    }

    private int scanHabits(Long userId) {
        int count = 0;
        for (HabitResponse habit : habitService.getAll(userId)) {
            // Only flag habits with a real streak on the line — avoids noise for brand-new habits
            boolean streakAtRisk = habit.getFrequency() == HabitFrequency.DAILY
                    && !habit.isLoggedForCurrentPeriod()
                    && habit.getCurrentStreak() > 0;

            if (streakAtRisk) {
                count += createIfNew(userId, NotificationType.HABIT_STREAK_AT_RISK, "HABIT", habit.getId(),
                        "Habit streak at risk: " + habit.getTitle(),
                        "You have a " + habit.getCurrentStreak() + "-day streak on \"" + habit.getTitle()
                                + "\" — log it today to keep it going.");
            }
        }
        return count;
    }

    private int scanDocuments(Long userId) {
        int count = 0;
        for (DocumentResponse doc : documentService.getAll(userId)) {
            if (doc.getStatus() == DocumentStatus.EXPIRING_SOON) {
                count += createIfNew(userId, NotificationType.DOCUMENT_EXPIRING, "DOCUMENT", doc.getId(),
                        "Document expiring soon: " + doc.getTitle(),
                        "\"" + doc.getTitle() + "\" expires on " + doc.getExpiryDate() + " (" + doc.getDaysUntilExpiry() + " days left).");
            } else if (doc.getStatus() == DocumentStatus.EXPIRED) {
                count += createIfNew(userId, NotificationType.DOCUMENT_EXPIRING, "DOCUMENT", doc.getId(),
                        "Document expired: " + doc.getTitle(),
                        "\"" + doc.getTitle() + "\" expired on " + doc.getExpiryDate() + ".");
            }
        }
        return count;
    }

    private int scanBudgets(Long userId) {
        int count = 0;
        for (BudgetResponse budget : budgetService.getAll(userId)) {
            if (budget.isAlertTriggered()) {
                count += createIfNew(userId, NotificationType.BUDGET_ALERT, "BUDGET", budget.getId(),
                        "Budget alert: " + budget.getCategory(),
                        "You've spent " + budget.getSpentPercentage() + "% of your " + budget.getBudgetMonth()
                                + " \"" + budget.getCategory() + "\" budget.");
            }
        }
        return count;
    }

    /**
     * Creates a notification only if one for this exact source+type hasn't already
     * been created today — prevents a daily scheduled scan from spamming duplicates.
     */
    private int createIfNew(Long userId, NotificationType type, String sourceType, Long sourceId,
                             String title, String message) {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

        boolean alreadyExists = notificationRepository
                .existsByUserIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtGreaterThanEqual(
                        userId, sourceType, sourceId, type, startOfToday);

        if (alreadyExists) {
            return 0;
        }

        User user = userRepository.getReferenceById(userId);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setSourceType(sourceType);
        notification.setSourceId(sourceId);
        notification.setRead(false);

        notificationRepository.save(notification);
        return 1;
    }
}