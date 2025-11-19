package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

/**
 * Service interface for User management
 *
 * @author Team
 * @version 1.0
 */
public interface UserService extends UserDetailsService{

    /**
     * Get user by ID
     * @param userId User UUID
     * @return ApiResponse containing UserDto
     */
    ApiResponse<UserDto> getUserById(UUID userId);

    /**
     * Get all users with pagination
     * @param pageable Pagination information
     * @return ApiResponse containing PageResponse of UserDto
     */
    ApiResponse<PageResponse<UserDto>> getAllUsers(Pageable pageable);

    /**
     * Toggle user active status (enable/disable)
     * @param userId User UUID
     * @return ApiResponse with success message
     */
    ApiResponse<Void> toggleUserStatus(UUID userId);

    /**
     * Update user's customer_id
     * Called by Customer service after customer profile creation
     * @param userId User UUID
     * @param customerId Customer UUID
     */
    void updateCustomerId(UUID userId, UUID customerId);
}
