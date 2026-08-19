package com.lifesync.habit.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.habit.dto.HabitLogResponse;
import com.lifesync.habit.dto.HabitRequest;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.entity.Habit;
import com.lifesync.habit.entity.HabitFrequency;
import com.lifesync.habit.entity.HabitLog;
import com.lifesync.habit.repository.HabitLogRepository;
import com.lifesync.habit.repository.HabitRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitServiceImpl implements HabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private HabitStreakCalculator streakCalculator;

    @Override
    @Transactional
    public HabitResponse create(Long userId, HabitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Habit habit = new Habit();
        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());
        habit.setUser(user);
        habit.setActive(true);

        if (request.getGoalId() != null) {
            Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
            habit.setGoal(goal);
        }

        Habit saved = habitRepository.save(habit);
        return buildResponse(saved);
    }

    @Override
    public List<HabitResponse> getAll(Long userId) {
        return habitRepository.findByUserIdAndActiveTrueOrderByTitleAsc(userId)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public HabitResponse getById(Long userId, Long habitId) {
        Habit habit = getOwnedHabit(userId, habitId);
        return buildResponse(habit);
    }

    @Override
    @Transactional
    public HabitResponse update(Long userId, Long habitId, HabitRequest request) {
        Habit habit = getOwnedHabit(userId, habitId);

        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());

        if (request.getGoalId() != null) {
            Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
            habit.setGoal(goal);
        } else {
            habit.setGoal(null);
        }

        Habit saved = habitRepository.save(habit);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long habitId) {
        Habit habit = getOwnedHabit(userId, habitId);
        habit.setActive(false); // soft delete — keeps streak history intact
        habitRepository.save(habit);
    }

    @Override
    @Transactional
    public HabitResponse logToday(Long userId, Long habitId) {
        Habit habit = getOwnedHabit(userId, habitId);
        LocalDate today = LocalDate.now();

        HabitLog log = habitLogRepository.findByHabitIdAndLogDate(habitId, today)
                .orElseGet(() -> {
                    HabitLog newLog = new HabitLog();
                    newLog.setHabit(habit);
                    newLog.setLogDate(today);
                    return newLog;
                });

        log.setCompleted(true);
        habitLogRepository.save(log);

        return buildResponse(habit);
    }

    @Override
    @Transactional
    public HabitResponse unlogToday(Long userId, Long habitId) {
        Habit habit = getOwnedHabit(userId, habitId);
        LocalDate today = LocalDate.now();

        habitLogRepository.findByHabitIdAndLogDate(habitId, today)
                .ifPresent(habitLogRepository::delete);

        return buildResponse(habit);
    }

    @Override
    public List<HabitLogResponse> getLogs(Long userId, Long habitId) {
        getOwnedHabit(userId, habitId); // ownership check
        return habitLogRepository.findByHabitIdOrderByLogDateDesc(habitId)
                .stream().map(HabitLogResponse::from).collect(Collectors.toList());
    }

    private Habit getOwnedHabit(Long userId, Long habitId) {
        return habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));
    }

    private HabitResponse buildResponse(Habit habit) {
        HabitResponse dto = new HabitResponse();
        dto.setId(habit.getId());
        dto.setTitle(habit.getTitle());
        dto.setDescription(habit.getDescription());
        dto.setFrequency(habit.getFrequency());
        dto.setActive(habit.isActive());
        dto.setCreatedAt(habit.getCreatedAt());
        dto.setUpdatedAt(habit.getUpdatedAt());

        if (habit.getGoal() != null) {
            dto.setGoalId(habit.getGoal().getId());
            dto.setGoalTitle(habit.getGoal().getTitle());
        }

        dto.setCurrentStreak(streakCalculator.calculateCurrentStreak(habit));
        dto.setAdherencePercentage(streakCalculator.calculateAdherencePercentage(habit));

        LocalDate today = LocalDate.now();
        if (habit.getFrequency() == HabitFrequency.DAILY) {
            dto.setLoggedForCurrentPeriod(habitLogRepository.findByHabitIdAndLogDate(habit.getId(), today).isPresent());
        } else {
            // weekly — check if any log falls in the current ISO week
            boolean loggedThisWeek = habitLogRepository.findByHabitIdOrderByLogDateDesc(habit.getId())
                    .stream()
                    .anyMatch(log -> log.isCompleted()
                            && log.getLogDate().get(IsoFields.WEEK_BASED_YEAR) == today.get(IsoFields.WEEK_BASED_YEAR)
                            && log.getLogDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            dto.setLoggedForCurrentPeriod(loggedThisWeek);
        }

        return dto;
    }
}