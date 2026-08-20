package com.lifesync.document.entity;

/** Computed, not stored — recalculated on every read based on expiryDate. */
public enum DocumentStatus {
    ACTIVE,
    EXPIRING_SOON,
    EXPIRED,
    NO_EXPIRY
}