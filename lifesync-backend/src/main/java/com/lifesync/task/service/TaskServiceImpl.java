package com.lifesync.task.service;

import com.lifesync.common.exception.BadRequestException;
import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.entity.GoalMilestone;
import com.lifesync.goal.repository.GoalMilestoneRepository;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.task.dto.TaskRequest;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.entity.RecurrencePattern;
import com.lifesync.task.entity.Task;
import com.lifesync.task.entity.TaskStatus;
import com.lifesync.task.repository.TaskRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalMilestoneRepository milestoneRepository;

    @Override
    @Transactional
    public TaskResponse create(Long userId, TaskRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = new Task();
        applyRequestToTask(task, request, userId);
        task.setUser(user);
        task.setStatus(TaskStatus.TODO);

        Task saved = taskRepository.save(task);
        return buildResponse(saved);
    }

    @Override
    public List<TaskResponse> getAll(Long userId) {
        return taskRepository.findByUserIdOrderByDueDateAsc(userId)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getByStatus(Long userId, String status) {
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status. Use TODO, IN_PROGRESS, or DONE");
        }
        return taskRepository.findByUserIdAndStatusOrderByDueDateAsc(userId, taskStatus)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getOverdue(Long userId) {
        return taskRepository.findByUserIdAndDueDateLessThanAndStatusNot(userId, LocalDate.now(), TaskStatus.DONE)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }
    @Override
    public List<TaskResponse> getDueToday(Long userId) {
        return taskRepository.findByUserIdAndDueDate(userId, LocalDate.now())
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public TaskResponse getById(Long userId, Long taskId) {
        Task task = getOwnedTask(userId, taskId);
        return buildResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse update(Long userId, Long taskId, TaskRequest request) {
        Task task = getOwnedTask(userId, taskId);
        applyRequestToTask(task, request, userId);
        Task saved = taskRepository.save(task);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long taskId) {
        Task task = getOwnedTask(userId, taskId);
        taskRepository.delete(task);
    }
    @Override
    @Transactional
    public TaskResponse markComplete(Long userId, Long taskId) {
        Task task = getOwnedTask(userId, taskId);

        if (task.getDependsOn() != null && task.getDependsOn().getStatus() != TaskStatus.DONE) {
            throw new BadRequestException(
                    "Cannot complete this task until its dependency \"" + task.getDependsOn().getTitle() + "\" is done");
        }

        task.setStatus(TaskStatus.DONE);
        task.setCompletedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);

        if (saved.isRecurring() && saved.getRecurrencePattern() != RecurrencePattern.NONE) {
            generateNextOccurrence(saved);
        }

        return buildResponse(saved);
    }

    /**
     * Recurring task completion engine — creates the next occurrence with a
     * pushed-forward due date, as long as the recurrence hasn't reached its end date.
     */ private void generateNextOccurrence(Task completedTask) {
         if (completedTask.getDueDate() == null) {
             return; // nothing to base the next occurrence on
         }

         LocalDate nextDueDate = switch (completedTask.getRecurrencePattern()) {
             case DAILY -> completedTask.getDueDate().plusDays(1);
             case WEEKLY -> completedTask.getDueDate().plusWeeks(1);
             case MONTHLY -> completedTask.getDueDate().plusMonths(1);
             default -> null;
         };

         if (nextDueDate == null) {
             return;
         }

         if (completedTask.getRecurrenceEndDate() != null && nextDueDate.isAfter(completedTask.getRecurrenceEndDate())) {
             return; // recurrence window has ended
         }
         Task next = new Task();
         next.setUser(completedTask.getUser());
         next.setTitle(completedTask.getTitle());
         next.setDescription(completedTask.getDescription());
         next.setGoal(completedTask.getGoal());
         next.setMilestone(completedTask.getMilestone());
         next.setDueDate(nextDueDate);
         next.setPriority(completedTask.getPriority());
         next.setEstimatedMinutes(completedTask.getEstimatedMinutes());
         next.setRecurring(true);
         next.setRecurrencePattern(completedTask.getRecurrencePattern());
         next.setRecurrenceEndDate(completedTask.getRecurrenceEndDate());
         next.setStatus(TaskStatus.TODO);
         // Note: dependsOn intentionally not copied — each recurrence starts independent

         taskRepository.save(next);
     }

     private void applyRequestToTask(Task task, TaskRequest request, Long userId) {
         task.setTitle(request.getTitle());
         task.setDescription(request.getDescription());
         task.setDueDate(request.getDueDate());
         task.setPriority(request.getPriority());
         task.setEstimatedMinutes(request.getEstimatedMinutes());
         task.setRecurring(request.isRecurring());
         task.setRecurrencePattern(request.getRecurrencePattern());
         task.setRecurrenceEndDate(request.getRecurrenceEndDate());
         if (request.getGoalId() != null) {
             Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                     .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
             task.setGoal(goal);
         } else {
             task.setGoal(null);
         }

         if (request.getMilestoneId() != null) {
             GoalMilestone milestone = milestoneRepository.findById(request.getMilestoneId())
                     .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));
             task.setMilestone(milestone);
         } else {
             task.setMilestone(null);
         }

         if (request.getDependsOnTaskId() != null) {
             if (task.getId() != null && request.getDependsOnTaskId().equals(task.getId())) {
                 throw new BadRequestException("A task cannot depend on itself");
             }
             Task dependsOn = taskRepository.findByIdAndUserId(request.getDependsOnTaskId(), userId)
                     .orElseThrow(() -> new ResourceNotFoundException("Dependency task not found"));
             task.setDependsOn(dependsOn);
         } else {
             task.setDependsOn(null);
         }
     } private Task getOwnedTask(Long userId, Long taskId) {
         return taskRepository.findByIdAndUserId(taskId, userId)
                 .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
     }

     private TaskResponse buildResponse(Task task) {
         TaskResponse dto = new TaskResponse();
         dto.setId(task.getId());
         dto.setTitle(task.getTitle());
         dto.setDescription(task.getDescription());
         dto.setDueDate(task.getDueDate());
         dto.setPriority(task.getPriority());
         dto.setStatus(task.getStatus());
         dto.setEstimatedMinutes(task.getEstimatedMinutes());
         dto.setRecurring(task.isRecurring());
         dto.setRecurrencePattern(task.getRecurrencePattern());
         dto.setRecurrenceEndDate(task.getRecurrenceEndDate());
         dto.setCompletedAt(task.getCompletedAt());
         dto.setCreatedAt(task.getCreatedAt());
         dto.setUpdatedAt(task.getUpdatedAt());
         boolean overdue = task.getDueDate() != null
                 && task.getDueDate().isBefore(LocalDate.now())
                 && task.getStatus() != TaskStatus.DONE;
         dto.setOverdue(overdue);

         if (task.getGoal() != null) {
             dto.setGoalId(task.getGoal().getId());
             dto.setGoalTitle(task.getGoal().getTitle());
         }

         if (task.getMilestone() != null) {
             dto.setMilestoneId(task.getMilestone().getId());
             dto.setMilestoneTitle(task.getMilestone().getTitle());
         }

         if (task.getDependsOn() != null) {
             dto.setDependsOnTaskId(task.getDependsOn().getId());
             dto.setDependsOnTaskTitle(task.getDependsOn().getTitle());
             dto.setDependsOnCompleted(task.getDependsOn().getStatus() == TaskStatus.DONE);
         }

         return dto;
     }
 }