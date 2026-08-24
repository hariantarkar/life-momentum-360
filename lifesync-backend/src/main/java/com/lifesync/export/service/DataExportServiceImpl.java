package com.lifesync.export.service;

import com.lifesync.auth.dto.UserSummary;
import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.document.service.DocumentService;
import com.lifesync.export.dto.UserDataExportResponse;
import com.lifesync.finance.service.BudgetService;
import com.lifesync.finance.service.ExpenseService;
import com.lifesync.finance.service.IncomeService;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.service.GoalService;
import com.lifesync.habit.service.HabitService;
import com.lifesync.learning.service.LearningPathService;
import com.lifesync.learning.service.SkillService;
import com.lifesync.lifearea.service.LifeAreaService;
import com.lifesync.task.service.TaskService;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataExportServiceImpl implements DataExportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LifeAreaService lifeAreaService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private LearningPathService learningPathService;

    @Override
    public UserDataExportResponse exportUserData(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch goals with full milestone detail (getById includes milestones; getAll doesn't)
        List<GoalResponse> goalsWithMilestones = goalService.getAll(userId).stream()
                .map(g -> goalService.getById(userId, g.getId()))
                .collect(Collectors.toList());

        UserDataExportResponse export = new UserDataExportResponse();
        export.setExportedAt(LocalDateTime.now());
        export.setProfile(UserSummary.from(user));
        export.setLifeAreas(lifeAreaService.getAll(userId));
        export.setGoals(goalsWithMilestones);
        export.setTasks(taskService.getAll(userId));
        export.setHabits(habitService.getAll(userId));
        export.setIncomes(incomeService.getAll(userId));
        export.setExpenses(expenseService.getAll(userId));
        export.setBudgets(budgetService.getAll(userId));
        export.setDocuments(documentService.getAll(userId));
        export.setSkills(skillService.getAll(userId));
        export.setLearningPaths(learningPathService.getAll(userId));

        return export;
    }
}