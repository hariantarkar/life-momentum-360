package com.lifesync.habit.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.habit.dto.HabitLogResponse;
import com.lifesync.habit.dto.HabitRequest;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.service.HabitService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @PostMapping
    public ResponseEntity<ApiResponse<HabitResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody HabitRequest request) {

        HabitResponse response = habitService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Habit created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HabitResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(habitService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(habitService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody HabitRequest request) {

        HabitResponse response = habitService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Habit updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        habitService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Habit deleted", null));
    }

    @PatchMapping("/{id}/log")
    public ResponseEntity<ApiResponse<HabitResponse>> logToday(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        HabitResponse response = habitService.logToday(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Habit logged for today", response));
    }

    @DeleteMapping("/{id}/log")
    public ResponseEntity<ApiResponse<HabitResponse>> unlogToday(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        HabitResponse response = habitService.unlogToday(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Today's log removed", response));
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<ApiResponse<List<HabitLogResponse>>> getLogs(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(habitService.getLogs(principal.getId(), id)));
    }
}