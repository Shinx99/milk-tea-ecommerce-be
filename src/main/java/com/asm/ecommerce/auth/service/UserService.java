package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.customer.service.CustomerService;  // ⭐ Inject Service
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    /*
    private final UserRepository userRepository;
    private final CustomerService customerService;  // ⭐ Inject service từ feature khác
    
    public ApiResponse<UserDetailResponse> getUserDetail(UUID userId) {
        // 1. Lấy data từ feature hiện tại (auth)
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // 2. ⭐ Gọi service của feature khác, nhận DTO
        CustomerResponse customer = customerService.getCustomerById(user.getCustomerId());
        
        // 3. Kết hợp data từ nhiều feature vào DTO response
        UserDetailResponse response = UserDetailResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .isActive(user.getIsActive())
            .roleId(user.getRoleId())
            .roleName(user.getRole().getRole())
            // ⭐ Data từ customer feature (qua DTO)
            .customerId(customer.getId())
            .customerName(customer.getFullname())
            .customerPhone(customer.getPhone())
            .customerEmail(customer.getEmail())
            .build();
        
        return ApiResponse.<UserDetailResponse>builder()
            .success(true)
            .data(response)
            .build();
    }
    */
}
