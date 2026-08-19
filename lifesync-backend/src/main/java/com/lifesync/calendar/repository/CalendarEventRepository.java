package com.lifesync.calendar.repository;

import com.lifesync.calendar.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    List<CalendarEvent> findByUserIdOrderByStartTimeAsc(Long userId);

    Optional<CalendarEvent> findByIdAndUserId(Long id, Long userId);

    List<CalendarEvent> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    /**
     * Two time ranges overlap when: existing.start < new.end AND existing.end > new.start.
     * excludeId lets an update check for conflicts against every OTHER event without
     * flagging itself; pass -1 when creating a brand new event.
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.user.id = :userId AND e.id <> :excludeId " +
           "AND e.startTime < :endTime AND e.endTime > :startTime")
    List<CalendarEvent> findOverlapping(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);
}