package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.PasswordResetToken;
import com.asm.ecommerce.auth.domain.Role;
import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.repository.PasswordResetTokenRepository;
import com.asm.ecommerce.auth.repository.UserRepository;
import com.asm.ecommerce.auth.repository.RoleRepository;
import com.asm.ecommerce.auth.dto.request.LoginRequest;
import com.asm.ecommerce.auth.dto.request.RegisterRequest;
import com.asm.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.customer.CreateCustomerRequest;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.exception.BadRequestException;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import com.asm.ecommerce.shared.exception.UnauthorizedException;
import com.asm.ecommerce.shared.security.JwtUtil;
import com.asm.ecommerce.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceIml implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Autowired
    private JwtUtil jwtUtil;



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

        // Generate JWT
        String token = jwtUtil.generateToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().getRole()
        );



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

    //Fixed Done
    // auth/service/AuthServiceImpl.java
    @Override
    @Transactional
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        log.info("Starting user registration for email: {}", request.getEmail());

        try {
            // 1. Validate
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already exists");
            }

            // 2. Get roleId
            UUID userRoleId = roleRepository.findByRole("customer")
                    .map(Role::getId)
                    .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));

            // 3. Create User
            //Use Mapper ------> entity
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            User user = userMapper.toEntity( request, userRoleId, encodedPassword);

            /*User user = User.builder()
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .roleId(userRoleId)
                    .isActive(true)
                    .build();*/

            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());

            // 4. Create Customer
            CreateCustomerRequest customerRequest = CreateCustomerRequest.builder()
                    .userId(savedUser.getId())
                    .fullName(request.getFullname())
                    .phone(request.getPhone())
                    .build();

            CustomerDTO customer = customerService.createCustomer(customerRequest);

            // 5. Generate token
            String token = "Token-" + UUID.randomUUID();

            // 6. Build response
            AuthResponse response = userMapper.toAuthResponse(
                    savedUser,
                    token
            );

            /*AuthResponse response = AuthResponse.builder()
                    .userId(savedUser.getId())
                    .email(savedUser.getEmail())
                    .roleId(savedUser.getRoleId())
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(86400L)
                    .customerId(customer.getId())
                    .build();*/

            log.info("User registration completed successfully: {}", savedUser.getEmail());

            return ApiResponse.<AuthResponse>builder()
                    .success(true)
                    .message("Registration successful")
                    .data(response)
                    .build();

        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation during registration", e);
            throw new BadRequestException("Registration failed: Data conflict");
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        }
    }




    @Override
    @Transactional
    public ApiResponse<Void> forgotPassword(String email) {
        // 1. Tìm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        // Tìm tất cả các token của user đã tạo mà chưa sử dụng
        List<PasswordResetToken> oldTokens = passwordResetTokenRepository.findAllActiveTokensByUserId(user.getId(), Instant.now());

        if(!oldTokens.isEmpty()){
            log.info("Vô hiệu hóa {} token cũ của user: {}", oldTokens.size(), email);
            for(PasswordResetToken oldToken : oldTokens){
                oldToken.setUsed(true);                 // đánh dấu là true thể hiện việc đã sử dụng token để tránh bị tái sử dụng token cũ
            }
            passwordResetTokenRepository.saveAll(oldTokens);
        }

        // 2. Tạo token đặt lại mật khẩu (random secure token)
        String resetToken = UUID.randomUUID().toString();

        // 3. Lưu token và thời gian hết hạn vào DB
        PasswordResetToken token = PasswordResetToken.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(resetToken)
                .expiryDate(Instant.now().plus(Duration.ofHours(1)))
                .used(false)
                .build();

        passwordResetTokenRepository.save(token);

        // 4. Gửi email chứa link reset (chứa token)
        String resetLink = frontendBaseUrl + "/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        // 5. Trả về response thành công
        log.info("Password reset requested for: {}", email);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset instructions sent to your email")
                .build();
    }


    @Override
    public ApiResponse<Void> resetPassword(String token, String newPassword) {
        // TODO: Verify token and reset password

        //Tìm Token trong db
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        //Check date hsd
        if(prt.getUsed() || prt.getExpiryDate().isBefore(Instant.now())){
            throw new BadRequestException("Token expired or already used");
        }

        //Tìm user liên quan
        User user = userRepository.findById(prt.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));


        //Mã hóa new password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);

        //Đánh dấu token đã dùng
        prt.setUsed(true);
        passwordResetTokenRepository.save(prt);

        log.info("Password reset with token: {}", token);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successful")
                .build();
    }


}

