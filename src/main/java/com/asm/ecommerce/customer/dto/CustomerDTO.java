package com.asm.ecommerce.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for Customer response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private UUID id;
    private String email;
    private String phone;
    private String fullname;
    private UUID userId;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
