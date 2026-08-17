package com.lifesync.lifearea.dto;

import com.lifesync.lifearea.entity.LifeArea;
import java.time.LocalDateTime;

public class LifeAreaResponse {

    private Long id;
    private String name;
    private String description;
    private String colorCode;
    private String icon;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LifeAreaResponse() {
    }

    public static LifeAreaResponse from(LifeArea area) {
        LifeAreaResponse dto = new LifeAreaResponse();
        dto.id = area.getId();
        dto.name = area.getName();
        dto.description = area.getDescription();
        dto.colorCode = area.getColorCode();
        dto.icon = area.getIcon();
        dto.active = area.isActive();
        dto.createdAt = area.getCreatedAt();
        dto.updatedAt = area.getUpdatedAt();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}