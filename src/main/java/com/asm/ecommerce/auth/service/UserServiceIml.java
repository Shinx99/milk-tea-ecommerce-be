package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.repository.UserRepository;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceIml implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<UserDto> getUserById(UUID userId) {
        log.debug("Getting user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return ApiResponse.<UserDto>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(userMapper.toDto(user))
                .build();
    }

    @Override
    public ApiResponse<PageResponse<UserDto>> getAllUsers(Pageable pageable) {
        log.debug("Getting all users with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> userPage = userRepository.findAll(pageable);

        PageResponse<UserDto> pageResponse = PageResponse.<UserDto>builder()
                .content(userPage.getContent().stream()
                        .map(userMapper::toDto)
                        .toList())
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .last(userPage.isLast())
                .build();

        return ApiResponse.<PageResponse<UserDto>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(pageResponse)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Void> toggleUserStatus(UUID userId) {
        log.debug("Toggling user status for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setIsActive(!user.getIsActive());
        userRepository.save(user);

        String status = user.getIsActive() ? "activated" : "deactivated";
        log.info("User {} successfully {}", userId, status);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User status updated successfully")
                .build();
    }


    // ... existing methods

    //Vuong
    @Override
    @Transactional
    public void updateCustomerId(UUID userId, UUID customerId) {
        log.debug("Updating customer_id for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        //user.setCustomerId(customerId);
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);

        log.info("Successfully updated customer_id {} for user {}", customerId, userId);
    }

    @Override
    @Transactional(readOnly = true) // (Thêm @Transactional nếu có)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Dùng phương thức bạn đã có trong UserRepository
        return (UserDetails) userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
