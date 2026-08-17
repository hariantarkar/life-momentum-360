package com.lifesync.goal.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.goal.dto.MilestoneRequest;
import com.lifesync.goal.dto.MilestoneResponse;
import com.lifesync.goal.service.MilestoneService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals/{goalId}/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<ApiResponse<MilestoneResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId,
            @Valid @RequestBody MilestoneRequest request) {

        MilestoneResponse response = milestoneService.create(principal.getId(), goalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Milestone created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MilestoneResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId) {

        return ResponseEntity.ok(ApiResponse.success(milestoneService.getAll(principal.getId(), goalId)));
    }

    @PutMapping("/{milestoneId}")
    public ResponseEntity<ApiResponse<MilestoneResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId,
            @PathVariable Long milestoneId,
            @Valid @RequestBody MilestoneRequest request) {

        MilestoneResponse response = milestoneService.update(principal.getId(), goalId, milestoneId, request);
        return ResponseEntity.ok(ApiResponse.success("Milestone updated", response));
    }

    @PatchMapping("/{milestoneId}/toggle-complete")
    public ResponseEntity<ApiResponse<MilestoneResponse>> toggleComplete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId,
            @PathVariable Long milestoneId) {

        MilestoneResponse response = milestoneService.toggleComplete(principal.getId(), goalId, milestoneId);
        return ResponseEntity.ok(ApiResponse.success("Milestone status toggled", response));
    }

    @DeleteMapping("/{milestoneId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long goalId,
            @PathVariable Long milestoneId) {

        milestoneService.delete(principal.getId(), goalId, milestoneId);
        return ResponseEntity.ok(ApiResponse.success("Milestone deleted", null));
    }
}
