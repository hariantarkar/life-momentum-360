package com.lifesync.calendar.service;

import com.lifesync.calendar.dto.CalendarEventRequest;
import com.lifesync.calendar.dto.CalendarEventResponse;
import com.lifesync.calendar.entity.CalendarEvent;
import com.lifesync.calendar.repository.CalendarEventRepository;
import com.lifesync.common.exception.BadRequestException;
import com.lifesync.common.exception.ConflictException;
import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.goal.entity.Goal;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.task.entity.Task;
import com.lifesync.task.repository.TaskRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarEventServiceImpl implements CalendarEventService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public CalendarEventResponse create(Long userId, CalendarEventRequest request) {

        validateTimeRange(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!request.isAllowOverlap()) {
            checkForConflicts(userId, request.getStartTime(), request.getEndTime(), -1L);
        }

        CalendarEvent event = new CalendarEvent();
        applyRequestToEvent(event, request, userId);
        event.setUser(user);

        CalendarEvent saved = calendarEventRepository.save(event);
        return buildResponse(saved);
    }

    @Override
    public List<CalendarEventResponse> getAll(Long userId) {
        return calendarEventRepository.findByUserIdOrderByStartTimeAsc(userId)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getInRange(Long userId, LocalDateTime from, LocalDateTime to) {
        return calendarEventRepository.findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(userId, from, to)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Override
    public CalendarEventResponse getById(Long userId, Long eventId) {
        CalendarEvent event = getOwnedEvent(userId, eventId);
        return buildResponse(event);
    }

    @Override
    @Transactional
    public CalendarEventResponse update(Long userId, Long eventId, CalendarEventRequest request) {
        validateTimeRange(request);

        CalendarEvent event = getOwnedEvent(userId, eventId);

        if (!request.isAllowOverlap()) {
            checkForConflicts(userId, request.getStartTime(), request.getEndTime(), eventId);
        }

        applyRequestToEvent(event, request, userId);
        CalendarEvent saved = calendarEventRepository.save(event);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long eventId) {
        CalendarEvent event = getOwnedEvent(userId, eventId);
        calendarEventRepository.delete(event);
    }

    private void validateTimeRange(CalendarEventRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }
    }

    /**
     * The core conflict-detection check: looks for any other event belonging to this
     * user whose time range overlaps the requested one. excludeId lets updates ignore
     * the event's own existing row.
     */
    private void checkForConflicts(Long userId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        List<CalendarEvent> conflicts = calendarEventRepository.findOverlapping(userId, startTime, endTime, excludeId);
        if (!conflicts.isEmpty()) {
            String conflictTitles = conflicts.stream()
                    .map(CalendarEvent::getTitle)
                    .collect(Collectors.joining(", "));
            throw new ConflictException(
                    "This time slot conflicts with: " + conflictTitles + ". Set allowOverlap=true to override.");
        }
    }

    private void applyRequestToEvent(CalendarEvent event, CalendarEventRequest request, Long userId) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());

        if (request.getGoalId() != null) {
            Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
            event.setGoal(goal);
        } else {
            event.setGoal(null);
        }

        if (request.getTaskId() != null) {
            Task task = taskRepository.findByIdAndUserId(request.getTaskId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
            event.setTask(task);
        } else {
            event.setTask(null);
        }
    }

    private CalendarEvent getOwnedEvent(Long userId, Long eventId) {
        return calendarEventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar event not found"));
    }

    private CalendarEventResponse buildResponse(CalendarEvent event) {
        CalendarEventResponse dto = new CalendarEventResponse();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventType(event.getEventType());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setLocation(event.getLocation());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());

        if (event.getGoal() != null) {
            dto.setGoalId(event.getGoal().getId());
            dto.setGoalTitle(event.getGoal().getTitle());
        }

        if (event.getTask() != null) {
            dto.setTaskId(event.getTask().getId());
            dto.setTaskTitle(event.getTask().getTitle());
        }

        return dto;
    }
}