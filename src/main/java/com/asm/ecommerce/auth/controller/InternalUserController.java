package com.asm.ecommerce.auth.controller;

import com.asm.ecommerce.auth.service.UserService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Internal API for inter-service communication
 * Should be secured or restricted to internal network
 */
@Slf4j
@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PutMapping("/{userId}/customer")
    public ResponseEntity<ApiResponse<Void>> updateCustomerId(
            @PathVariable UUID userId,
            @RequestBody UpdateCustomerIdRequest request) {
        
        log.info("Internal API: Updating customer_id for user: {}", userId);
        
        userService.updateCustomerId(userId, request.getCustomerId());
        
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .success(true)
                .message("Customer ID updated successfully")
                .build()
        );
    }

    @Data
    public static class UpdateCustomerIdRequest {
        private UUID customerId;
    }
}
