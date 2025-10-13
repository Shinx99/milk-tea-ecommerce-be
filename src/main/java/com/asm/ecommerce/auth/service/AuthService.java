package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.repository.UserRepository;
import com.asm.ecommerce.auth.repository.RoleRepository;
import com.asm.ecommerce.auth.dto.request.LoginRequest;
import com.asm.ecommerce.auth.dto.request.RegisterRequest;
import com.asm.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.exception.BadRequestException;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import com.asm.ecommerce.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    // TODO: Add JwtTokenProvider when implementing JWT

    public ApiResponse<AuthResponse> login(LoginRequest request) {
        // Find user with role
        User user = userRepository.findByEmailWithRole(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is disabled");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // TODO: Generate JWT token
        String token = "temporary-token-" + UUID.randomUUID();

        AuthResponse response = userMapper.toAuthResponse(user, token);

        log.info("User logged in successfully: {}", user.getEmail());

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }

    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Get default role (CUSTOMER)
        var defaultRole = roleRepository.findByRole("customer")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        // TODO: Create customer first (when customer feature is ready)
        // For now, use a temporary customer ID
        UUID tempCustomerId = UUID.randomUUID();
        log.warn("Using temporary customer ID: {}. Implement customer creation later.", tempCustomerId);

        // Create user
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roleId(defaultRole.getId())
                .customerId(tempCustomerId)
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Load user with role for response
        savedUser = userRepository.findByEmailWithRole(savedUser.getEmail()).orElseThrow();

        // TODO: Generate JWT token
        String token = "temporary-token-" + UUID.randomUUID();

        AuthResponse response = userMapper.toAuthResponse(savedUser, token);

        log.info("User registered successfully: {}", savedUser.getEmail());

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Registration successful")
                .data(response)
                .build();
    }

    public ApiResponse<Void> changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Verify new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(OffsetDateTime.now());

        userRepository.save(user);

        log.info("Password changed for user: {}", user.getEmail());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build();
    }

    public ApiResponse<Void> forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        // TODO: Generate reset token and send email
        log.info("Password reset requested for: {}", email);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset instructions sent to your email")
                .build();
    }

    public ApiResponse<Void> resetPassword(String token, String newPassword) {
        // TODO: Verify token and reset password
        log.info("Password reset with token: {}", token);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successful")
                .build();
    }
}
