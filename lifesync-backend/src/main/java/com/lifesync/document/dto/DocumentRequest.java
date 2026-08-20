package com.lifesync.document.dto;

import com.lifesync.document.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class DocumentRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private DocumentType documentType = DocumentType.OTHER;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    private int renewalReminderDays = 30;

    private String fileReference;
    private String notes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getRenewalReminderDays() {
        return renewalReminderDays;
    }

    public void setRenewalReminderDays(int renewalReminderDays) {
        this.renewalReminderDays = renewalReminderDays;
    }

    public String getFileReference() {
        return fileReference;
    }

    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}