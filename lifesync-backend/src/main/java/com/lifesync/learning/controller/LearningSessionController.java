package com.lifesync.learning.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.learning.dto.LearningSessionRequest;
import com.lifesync.learning.dto.LearningSessionResponse;
import com.lifesync.learning.service.LearningSessionService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-paths/{pathId}/sessions")
public class LearningSessionController {

    @Autowired
    private LearningSessionService learningSessionService;

    @PostMapping
    public ResponseEntity<ApiResponse<LearningSessionResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long pathId,
            @Valid @RequestBody LearningSessionRequest request) {

        LearningSessionResponse response = learningSessionService.create(principal.getId(), pathId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Session logged", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningSessionResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long pathId) {

        return ResponseEntity.ok(ApiResponse.success(learningSessionService.getAll(principal.getId(), pathId)));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long pathId,
            @PathVariable Long sessionId) {

        learningSessionService.delete(principal.getId(), pathId, sessionId);
        return ResponseEntity.ok(ApiResponse.success("Session deleted", null));
    }
}