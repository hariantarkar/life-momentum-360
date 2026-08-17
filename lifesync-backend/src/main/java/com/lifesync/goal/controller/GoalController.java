package com.lifesync.goal.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.goal.dto.GoalRequest;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.service.GoalService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public ResponseEntity<ApiResponse<GoalResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody GoalRequest request) {

        GoalResponse response = goalService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Goal created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(goalService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(goalService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody GoalRequest request) {

        GoalResponse response = goalService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Goal updated", response));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<GoalResponse>> markComplete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        GoalResponse response = goalService.markComplete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Goal marked complete", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        goalService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Goal deleted", null));
    }
}