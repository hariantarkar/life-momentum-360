package com.lifesync.learning.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.learning.dto.SkillRequest;
import com.lifesync.learning.dto.SkillResponse;
import com.lifesync.learning.service.SkillService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SkillRequest request) {

        SkillResponse response = skillService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Skill added", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(skillService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(skillService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request) {

        SkillResponse response = skillService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Skill updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        skillService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted", null));
    }
}