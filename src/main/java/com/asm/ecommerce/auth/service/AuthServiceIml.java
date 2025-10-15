package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.repository.UserRepository;
import com.asm.ecommerce.auth.repository.RoleRepository;
import com.asm.ecommerce.auth.dto.request.LoginRequest;
import com.asm.ecommerce.auth.dto.request.RegisterRequest;
import com.asm.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.customer.service.CustomerService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.event.UserRegisteredEvent;
import com.asm.ecommerce.shared.exception.BadRequestException;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import com.asm.ecommerce.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceIml implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    CustomerService customerService;


    // TODO: Add JwtTokenProvider when implementing JWT

    @Override
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
        String token = "Token-" + UUID.randomUUID();

        AuthResponse response = userMapper.toAuthResponse(user, token);

        log.info("User logged in successfully: {}", user.getEmail());

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<Void> changePassword(UUID userId, ChangePasswordRequest request) {
        // 1. Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            log.warn("Failed password change attempt for user: {}", user.getEmail());
            throw new BadRequestException("Current password is incorrect");
        }

        // 3. Validate: new password khác current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // 4. Validate: newPassword và confirmPassword phải khớp
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        // 5. Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getEmail());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build();
    }


    @Override
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

    @Override
    public ApiResponse<Void> resetPassword(String token, String newPassword) {
        // TODO: Verify token and reset password
        log.info("Password reset with token: {}", token);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successful")
                .build();
    }



    // Đang xử lý   / còn ERROR
    @Override
    @Transactional
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        log.info("Starting user registration for email: {}", request.getEmail());

        // 1. Validate email doesn't exist
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new BadRequestException("Email already exists");
        }

        // 2. Get default customer role
        var customerRole = roleRepository.findByRole("customer")
                .orElseThrow(() -> new ResourceNotFoundException("Customer role not found"));

        // 3. Create User entity
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roleId(customerRole.getId())
                .customerId(null) // ✅ Will be set by Customer module via event
                .isActive(false) // Inactive until email verification
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // 4. Save user to database
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        // 5. ✅ Publish UserRegisteredEvent for async customer creation
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullname(request.getFullname())
                .phone(request.getPhone())
                .registeredAt(Instant.now())
                .build();

        eventPublisher.publishEvent(event);
        log.info("UserRegisteredEvent published for user: {}", savedUser.getEmail());

        // 6. Prepare response (without waiting for customer creation)
        String token = "temporary-token-" + UUID.randomUUID(); // TODO: Generate real JWT

        AuthResponse response = AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .token(token)
                .build();

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Registration successful. Your customer profile is being created.")
                .data(response)
                .build();
    }
}

