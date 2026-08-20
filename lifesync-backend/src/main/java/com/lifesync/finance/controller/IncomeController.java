package com.lifesync.finance.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.finance.dto.IncomeRequest;
import com.lifesync.finance.dto.IncomeResponse;
import com.lifesync.finance.service.IncomeService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @PostMapping
    public ResponseEntity<ApiResponse<IncomeResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody IncomeRequest request) {

        IncomeResponse response = incomeService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Income recorded", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncomeResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(incomeService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(incomeService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequest request) {

        IncomeResponse response = incomeService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Income updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        incomeService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Income deleted", null));
    }
}