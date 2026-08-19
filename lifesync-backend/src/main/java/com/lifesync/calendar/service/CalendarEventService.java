package com.lifesync.calendar.service;

import com.lifesync.calendar.dto.CalendarEventRequest;
import com.lifesync.calendar.dto.CalendarEventResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventService {
    CalendarEventResponse create(Long userId, CalendarEventRequest request);
    List<CalendarEventResponse> getAll(Long userId);
    List<CalendarEventResponse> getInRange(Long userId, LocalDateTime from, LocalDateTime to);
    CalendarEventResponse getById(Long userId, Long eventId);
    CalendarEventResponse update(Long userId, Long eventId, CalendarEventRequest request);
    void delete(Long userId, Long eventId);
}