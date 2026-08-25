package com.lifesync.task.service;

import com.lifesync.common.exception.BadRequestException;
import com.lifesync.goal.repository.GoalMilestoneRepository;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.entity.RecurrencePattern;
import com.lifesync.task.entity.Task;
import com.lifesync.task.entity.TaskStatus;
import com.lifesync.task.repository.TaskRepository;
import com.lifesync.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests the task dependency-blocking and recurring-task auto-generation logic —
 * the two "interesting" rules in the task engine, as opposed to plain CRUD.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalMilestoneRepository milestoneRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task buildTask(Long id, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle("Test task " + id);
        task.setStatus(status);
        task.setPriority(com.lifesync.task.entity.TaskPriority.MEDIUM);
        task.setRecurring(false);
        task.setRecurrencePattern(RecurrencePattern.NONE);
        return task;
    }

    @Test
    void markComplete_blockedWhenDependencyNotDone() {
        Task dependency = buildTask(1L, TaskStatus.TODO); // still not done
        Task blockedTask = buildTask(2L, TaskStatus.TODO);
        blockedTask.setDependsOn(dependency);

        when(taskRepository.findByIdAndUserId(2L, 100L)).thenReturn(Optional.of(blockedTask));

        assertThatThrownBy(() -> taskService.markComplete(100L, 2L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Cannot complete this task");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void markComplete_succeedsWhenDependencyIsDone() {
        Task dependency = buildTask(1L, TaskStatus.DONE); // completed
        Task task = buildTask(2L, TaskStatus.TODO);
        task.setDependsOn(dependency);

        when(taskRepository.findByIdAndUserId(2L, 100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponse response = taskService.markComplete(100L, 2L);

        assertThat(response.getStatus()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markComplete_recurringTask_generatesNextOccurrenceWithAdvancedDueDate() {
        Task task = buildTask(3L, TaskStatus.TODO);
        task.setDueDate(LocalDate.of(2026, 8, 17));
        task.setRecurring(true);
        task.setRecurrencePattern(RecurrencePattern.DAILY);

        when(taskRepository.findByIdAndUserId(3L, 100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        taskService.markComplete(100L, 3L);

        // First save = completing the original task, second save = the newly generated occurrence
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(2)).save(captor.capture());

        Task nextOccurrence = captor.getAllValues().get(1);
        assertThat(nextOccurrence.getDueDate()).isEqualTo(LocalDate.of(2026, 8, 18)); // pushed forward by 1 day
        assertThat(nextOccurrence.getStatus()).isEqualTo(TaskStatus.TODO); // fresh occurrence starts incomplete
        assertThat(nextOccurrence.isRecurring()).isTrue();
    }

    @Test
    void markComplete_recurringTask_stopsGeneratingPastRecurrenceEndDate() {
        Task task = buildTask(4L, TaskStatus.TODO);
        task.setDueDate(LocalDate.of(2026, 8, 17));
        task.setRecurring(true);
        task.setRecurrencePattern(RecurrencePattern.DAILY);
        task.setRecurrenceEndDate(LocalDate.of(2026, 8, 17)); // ends today — no more occurrences after this

        when(taskRepository.findByIdAndUserId(4L, 100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        taskService.markComplete(100L, 4L);

        // Only ONE save — completing the task itself, no second occurrence generated
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}