package com.lifesync.review.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.review.dto.WeeklyReviewResponse;
import com.lifesync.review.service.WeeklyReviewService;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weekly-reviews")
public class WeeklyReviewController {

    @Autowired
    private WeeklyReviewService weeklyReviewService;

    /** Optional ?weekStart=2026-08-17 (must be a Monday). Defaults to the current week if omitted. */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<WeeklyReviewResponse>> generate(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {

        WeeklyReviewResponse response = weeklyReviewService.generate(principal.getId(), weekStart);
        return ResponseEntity.ok(ApiResponse.success("Weekly review generated", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WeeklyReviewResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(weeklyReviewService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WeeklyReviewResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(weeklyReviewService.getById(principal.getId(), id)));
    }
}