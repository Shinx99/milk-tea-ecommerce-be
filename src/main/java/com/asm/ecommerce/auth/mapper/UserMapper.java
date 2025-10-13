package com.asm.ecommerce.auth.mapper;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .roleId(user.getRoleId())
                .roleName(user.getRole() != null ? user.getRole().getRole() : null)
                .customerId(user.getCustomerId())
                .build();
    }

    public AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours
                .roleId(user.getRoleId())
                .roleName(user.getRole() != null ? user.getRole().getRole() : null)
                .customerId(user.getCustomerId())
                .build();
    }
}
