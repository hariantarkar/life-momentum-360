package com.lifesync.review.repository;

import com.lifesync.review.entity.WeeklyReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyReviewRepository extends JpaRepository<WeeklyReview, Long> {

    List<WeeklyReview> findByUserIdOrderByWeekStartDateDesc(Long userId);

    Optional<WeeklyReview> findByUserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);

    Optional<WeeklyReview> findByIdAndUserId(Long id, Long userId);
}