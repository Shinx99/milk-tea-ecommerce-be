package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.repository.UserRepository;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ApiResponse<UserDto> getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiResponse.<UserDto>builder()
                .success(true)
                .data(userMapper.toDto(user))
                .build();
    }

    public ApiResponse<PageResponse<UserDto>> getAllUsers(Pageable pageable) {
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
                .data(pageResponse)
                .build();
    }

    @Transactional
    public ApiResponse<Void> toggleUserStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(!user.getIsActive());
        userRepository.save(user);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User status updated successfully")
                .build();
    }
}
