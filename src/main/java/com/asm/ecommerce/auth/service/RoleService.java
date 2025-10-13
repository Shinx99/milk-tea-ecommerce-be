package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.Role;
import com.asm.ecommerce.auth.repository.RoleRepository;
import com.asm.ecommerce.auth.dto.RoleDto;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public ApiResponse<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleRepository.findAll().stream()
                .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .role(role.getRole())
                        .description(role.getDescription())
                        .isActive(role.getIsActive())
                        .createdAt(role.getCreatedAt())
                        .build())
                .toList();

        return ApiResponse.<List<RoleDto>>builder()
                .success(true)
                .data(roles)
                .build();
    }

    public ApiResponse<RoleDto> getRoleById(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        RoleDto roleDto = RoleDto.builder()
                .id(role.getId())
                .role(role.getRole())
                .description(role.getDescription())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .build();

        return ApiResponse.<RoleDto>builder()
                .success(true)
                .data(roleDto)
                .build();
    }
}
