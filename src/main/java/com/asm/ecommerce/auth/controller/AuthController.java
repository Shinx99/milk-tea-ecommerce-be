package com.asm.ecommerce.auth.controller;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.dto.request.*;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.auth.service.AuthService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;     //Inject interface

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        log.info("Received registration request for email: {}", request.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication, // <<< Lấy đối tượng Authentication
            @Valid @RequestBody ChangePasswordRequest request) {

        // 2. Lấy principal (UserDetails)
        Object principal = authentication.getPrincipal();
        UUID userId;

        // 3. Kiểm tra kiểu và lấy ID
        if (principal instanceof User) { // <<< Khuyên dùng: Ép kiểu về entity User của bạn
            userId = ((User) principal).getId();
        } else if (principal instanceof UserDetails) { // <<< Dự phòng: Nếu principal là interface UserDetails
            // Giả định này yêu cầu getUsername() của bạn *thực sự* trả về ID dạng String
            // - điều này KHÔNG chuẩn. Ép kiểu về User tốt hơn.
            // Nếu BẮT BUỘC dùng getName(), đảm bảo JwtUtil đặt ID vào Subject VÀ UserDetails trả về ID cho getUsername()
            // userId = UUID.fromString(((UserDetails) principal).getUsername());

            // AN TOÀN HƠN: Nếu UserDetails có thể là String (ít khả năng với jwt filter)
            // userId = UUID.fromString(principal.toString());

            // CÁCH TỐT NHẤT LÀ ÉP KIỂU VỀ LỚP USER CỤ THỂ CỦA BẠN
            throw new IllegalStateException("Authentication principal không phải là instance của User entity");

        } else {
            // Xử lý trường hợp principal có thể chỉ là String (ví dụ: test đơn giản)
            // userId = UUID.fromString(principal.toString());
            throw new IllegalStateException("Kiểu principal không mong đợi: " + principal.getClass());
        }


        log.info("Nhận yêu cầu đổi mật khẩu cho userId (từ token principal): {}", userId);
        return ResponseEntity.ok(authService.changePassword(userId, request));
    }

    // .

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPassword request) {
        return ResponseEntity.ok(
                authService.resetPassword(request.getToken(), request.getNewPassword()));
    }
}
