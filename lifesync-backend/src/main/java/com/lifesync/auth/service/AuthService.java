package com.lifesync.auth.service;

import com.lifesync.auth.dto.AuthResponse;
import com.lifesync.auth.dto.LoginRequest;
import com.lifesync.auth.dto.RegisterRequest;
import com.lifesync.auth.dto.UserSummary;

public interface AuthService {
    UserSummary register(RegisterRequest request);
    AuthResponse login(LoginRequest request, String deviceInfo);
    AuthResponse refresh(String refreshToken);
    void logout(String refreshToken);
}
