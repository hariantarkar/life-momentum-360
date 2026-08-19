package com.lifesync.calendar.controller;

import com.lifesync.calendar.dto.CalendarEventRequest;
import com.lifesync.calendar.dto.CalendarEventResponse;
import com.lifesync.calendar.service.CalendarEventService;
import com.lifesync.common.response.ApiResponse;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;

    @PostMapping
    public ResponseEntity<ApiResponse<CalendarEventResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CalendarEventRequest request) {

        CalendarEventResponse response = calendarEventService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Event created", response));
    }

    /** Optional ?from=2026-08-01T00:00:00&to=2026-08-31T23:59:59 range filter. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CalendarEventResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<CalendarEventResponse> events = (from != null && to != null)
                ? calendarEventService.getInRange(principal.getId(), from, to)
                : calendarEventService.getAll(principal.getId());

        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CalendarEventResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(calendarEventService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CalendarEventResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody CalendarEventRequest request) {

        CalendarEventResponse response = calendarEventService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Event updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        calendarEventService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted", null));
    }
}