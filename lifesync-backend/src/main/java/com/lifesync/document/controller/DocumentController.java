package com.lifesync.document.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.document.dto.DocumentRequest;
import com.lifesync.document.dto.DocumentResponse;
import com.lifesync.document.service.DocumentService;
import com.lifesync.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody DocumentRequest request) {

        DocumentResponse response = documentService.create(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Document added", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(ApiResponse.success(documentService.getAll(principal.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(documentService.getById(principal.getId(), id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody DocumentRequest request) {

        DocumentResponse response = documentService.update(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Document updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        documentService.delete(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted", null));
    }
}