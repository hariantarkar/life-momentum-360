package com.lifesync.lifearea.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.lifearea.dto.LifeAreaRequest;
import com.lifesync.lifearea.dto.LifeAreaResponse;
import com.lifesync.lifearea.service.LifeAreaService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/life-areas")
public class LifeAreaController {

    @Autowired
    private LifeAreaService lifeAreaService;

    @PostMapping
    public ResponseEntity<ApiResponse<LifeAreaResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody LifeAreaRequest request) {

        LifeAreaResponse response = lifeAreaService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Life area created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LifeAreaResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        List<LifeAreaResponse> areas = lifeAreaService.getAll(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(areas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LifeAreaResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        LifeAreaResponse response = lifeAreaService.getById(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LifeAreaResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody LifeAreaRequest request) {

        LifeAreaResponse response = lifeAreaService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Life area updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        lifeAreaService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Life area deleted", null));
    }
}