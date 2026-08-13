package com.lifesync.auth.service;

import com.lifesync.auth.dto.*;
import com.lifesync.common.exception.BadRequestException;
import com.lifesync.common.exception.UnauthorizedException;
import com.lifesync.security.JwtService;
import com.lifesync.user.entity.RefreshToken;
import com.lifesync.user.entity.Role;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.RefreshTokenRepository;
import com.lifesync.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000; // 7 days

    @Override
    @Transactional
    public UserSummary register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        return UserSummary.from(saved);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request, String deviceInfo) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        return buildAuthResponse(user, deviceInfo);
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshTokenValue) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired or revoked, please login again");
        }

        // Rotate: revoke the old one, issue a brand new pair (better security than reusing it)
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = storedToken.getUser();
        return buildAuthResponse(user, storedToken.getDeviceInfo());
    }

    @Override
    @Transactional
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private AuthResponse buildAuthResponse(User user, String deviceInfo) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshTokenValue = jwtService.generateRefreshTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .deviceInfo(deviceInfo)
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_MS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresInMs(jwtService.getAccessTokenExpirationMs())
                .user(UserSummary.from(user))
                .build();
    }
}
