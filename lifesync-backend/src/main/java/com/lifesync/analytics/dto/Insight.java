package com.lifesync.analytics.dto;

public class Insight {

    private String category; // "GOAL", "TASK", "HABIT", "FINANCE"
    private InsightSeverity severity;
    private String message;

    public Insight() {
    }

    public Insight(String category, InsightSeverity severity, String message) {
        this.category = category;
        this.severity = severity;
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public InsightSeverity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }
}