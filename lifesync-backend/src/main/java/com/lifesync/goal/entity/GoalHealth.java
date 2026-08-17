package com.lifesync.goal.entity;

/**
 * Computed, not stored — recalculated every time a goal is read based on
 * progress percentage and how close the target date is.
 */
public enum GoalHealth {
    ON_TRACK,
    AT_RISK,
    OVERDUE,
    COMPLETED
}