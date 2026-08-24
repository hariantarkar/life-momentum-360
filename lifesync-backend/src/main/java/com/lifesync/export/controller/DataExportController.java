package com.lifesync.export.controller;

import com.lifesync.common.response.ApiResponse;
import com.lifesync.export.dto.UserDataExportResponse;
import com.lifesync.export.service.DataExportService;
import com.lifesync.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/export")
public class DataExportController {

    @Autowired
    private DataExportService dataExportService;

    /**
     * Full account data export. Postman will just show the JSON body; a browser hitting
     * this URL directly (with a valid Bearer token, e.g. via an extension) would download
     * it as a file thanks to the Content-Disposition header below.
     */
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<UserDataExportResponse>> exportData(
            @AuthenticationPrincipal UserPrincipal principal) {

        UserDataExportResponse export = dataExportService.exportUserData(principal.getId());

        String filename = "lifesync-export-" + LocalDate.now() + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(ApiResponse.success(export));
    }
}