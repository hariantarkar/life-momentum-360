package com.lifesync.learning.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.learning.dto.LearningPathRequest;
import com.lifesync.learning.dto.LearningPathResponse;
import com.lifesync.learning.service.LearningPathService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathController {

    @Autowired
    private LearningPathService learningPathService;

    @PostMapping
    public ResponseEntity<ApiResponse<LearningPathResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody LearningPathRequest request) {

        LearningPathResponse response = learningPathService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Learning path created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningPathResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(learningPathService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LearningPathResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(learningPathService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LearningPathResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody LearningPathRequest request) {

        LearningPathResponse response = learningPathService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Learning path updated", response));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<LearningPathResponse>> markComplete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        LearningPathResponse response = learningPathService.markComplete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Learning path marked complete", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        learningPathService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Learning path deleted", null));
    }
}

