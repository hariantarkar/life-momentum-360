package com.lifesync.document.service;

import com.lifesync.document.dto.DocumentRequest;
import com.lifesync.document.dto.DocumentResponse;

import java.util.List;

public interface DocumentService {
    DocumentResponse create(Long userId, DocumentRequest request);
    List<DocumentResponse> getAll(Long userId);
    DocumentResponse getById(Long userId, Long documentId);
    DocumentResponse update(Long userId, Long documentId, DocumentRequest request);
    void delete(Long userId, Long documentId);
}