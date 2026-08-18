package com.lifesync.task.service;

import com.lifesync.task.dto.TaskRequest;
import com.lifesync.task.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse create(Long userId, TaskRequest request);
    List<TaskResponse> getAll(Long userId);
    List<TaskResponse> getByStatus(Long userId, String status);
    List<TaskResponse> getOverdue(Long userId);
    List<TaskResponse> getDueToday(Long userId);
    TaskResponse getById(Long userId, Long taskId);
    TaskResponse update(Long userId, Long taskId, TaskRequest request);
    void delete(Long userId, Long taskId);
    TaskResponse markComplete(Long userId, Long taskId);
}