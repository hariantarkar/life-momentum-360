package com.lifesync.review.service;

import com.lifesync.review.dto.WeeklyReviewResponse;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyReviewService {
    WeeklyReviewResponse generate(Long userId, LocalDate weekStartDate);
    List<WeeklyReviewResponse> getAll(Long userId);
    WeeklyReviewResponse getById(Long userId, Long reviewId);
}