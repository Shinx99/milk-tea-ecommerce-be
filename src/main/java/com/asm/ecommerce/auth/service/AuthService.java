package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.asm.ecommerce.auth.dto.request.LoginRequest;
import com.asm.ecommerce.auth.dto.request.RegisterRequest;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;

import java.util.UUID;

public interface AuthService {
    ApiResponse<AuthResponse> login(LoginRequest request);
    ApiResponse<AuthResponse> register(RegisterRequest request);
    ApiResponse<Void> changePassword(UUID userId, ChangePasswordRequest request);
    ApiResponse<Void> forgotPassword(String email);
    ApiResponse<Void> resetPassword(String token, String newPassword);
}
