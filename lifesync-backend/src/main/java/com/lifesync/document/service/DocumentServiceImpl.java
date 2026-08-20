package com.lifesync.document.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.document.dto.DocumentRequest;
import com.lifesync.document.dto.DocumentResponse;
import com.lifesync.document.entity.Document;
import com.lifesync.document.repository.DocumentRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentExpiryCalculator expiryCalculator;

    @Override
    @Transactional
    public DocumentResponse create(Long userId, DocumentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = new Document();
        applyRequest(document, request);
        document.setUser(user);

        Document saved = documentRepository.save(document);
        return toResponse(saved);
    }

    @Override
    public List<DocumentResponse> getAll(Long userId) {
        return documentRepository.findByUserIdOrderByExpiryDateAsc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public DocumentResponse getById(Long userId, Long documentId) {
        return toResponse(getOwned(userId, documentId));
    }

    @Override
    @Transactional
    public DocumentResponse update(Long userId, Long documentId, DocumentRequest request) {
        Document document = getOwned(userId, documentId);
        applyRequest(document, request);
        return toResponse(documentRepository.save(document));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long documentId) {
        documentRepository.delete(getOwned(userId, documentId));
    }

    private Document getOwned(Long userId, Long documentId) {
        return documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    private void applyRequest(Document document, DocumentRequest request) {
        document.setTitle(request.getTitle());
        document.setDocumentType(request.getDocumentType());
        document.setIssueDate(request.getIssueDate());
        document.setExpiryDate(request.getExpiryDate());
        document.setRenewalReminderDays(request.getRenewalReminderDays());
        document.setFileReference(request.getFileReference());
        document.setNotes(request.getNotes());
    }

    private DocumentResponse toResponse(Document document) {
        DocumentResponse dto = new DocumentResponse();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setDocumentType(document.getDocumentType());
        dto.setIssueDate(document.getIssueDate());
        dto.setExpiryDate(document.getExpiryDate());
        dto.setRenewalReminderDays(document.getRenewalReminderDays());
        dto.setFileReference(document.getFileReference());
        dto.setNotes(document.getNotes());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());

        dto.setStatus(expiryCalculator.calculateStatus(document.getExpiryDate(), document.getRenewalReminderDays()));
        dto.setDaysUntilExpiry(expiryCalculator.calculateDaysUntilExpiry(document.getExpiryDate()));

        return dto;
    }
}