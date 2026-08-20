package com.lifesync.finance.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.finance.dto.BudgetRequest;
import com.lifesync.finance.dto.BudgetResponse;
import com.lifesync.finance.service.BudgetService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/finance/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BudgetRequest request) {

        BudgetResponse response = budgetService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Budget created", response));
    }

    /** Optional ?month=2026-08 filter. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        List<BudgetResponse> budgets = (month != null)
                ? budgetService.getByMonth(principal.getId(), month)
                : budgetService.getAll(principal.getId());

        return ResponseEntity.ok(ApiResponse.success(budgets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(budgetService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {

        BudgetResponse response = budgetService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Budget updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        budgetService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Budget deleted", null));
    }
}