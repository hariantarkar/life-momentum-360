package com.lifesync.export.service;

import com.lifesync.export.dto.UserDataExportResponse;

public interface DataExportService {
    UserDataExportResponse exportUserData(Long userId);
}