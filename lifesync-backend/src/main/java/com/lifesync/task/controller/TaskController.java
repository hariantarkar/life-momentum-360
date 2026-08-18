package com.lifesync.task.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.security.UserPrincipal;
import com.lifesync.task.dto.TaskRequest;
import com.lifesync.task.dto.TaskResponse;
import com.lifesync.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody TaskRequest request) {

        TaskResponse response = taskService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Task created", response));
    }
    /** Optional ?status=TODO|IN_PROGRESS|DONE query param to filter. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String status) {

        List<TaskResponse> tasks = (status == null)
                ? taskService.getAll(principal.getId())
                : taskService.getByStatus(principal.getId(), status);

        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getOverdue(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(taskService.getOverdue(principal.getId())));
    }

    @GetMapping("/due-today")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getDueToday(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(taskService.getDueToday(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(taskService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        TaskResponse response = taskService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated", response));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> markComplete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        TaskResponse response = taskService.markComplete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Task marked complete", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        taskService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Task deleted", null));
    }
}