package com.lifesync.auth.controller;

import com.lifesync.auth.dto.*;
import com.lifesync.auth.service.AuthService;
import com.lifesync.common.response.ApiResponse;
import com.lifesync.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserSummary>> register(@Valid @RequestBody RegisterRequest request) {
        UserSummary user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                             HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        AuthResponse response = authService.login(request, deviceInfo);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }

    /** Protected route — proves the JWT filter + SecurityContext wiring works end to end. */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(
                java.util.Map.of(
                        "id", principal.getId(),
                        "email", principal.getEmail(),
                        "authorities", principal.getAuthorities()
                )
        ));
    }
}
