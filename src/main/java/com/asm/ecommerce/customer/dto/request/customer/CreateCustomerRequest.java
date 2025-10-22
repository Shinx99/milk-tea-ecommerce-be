// customer/dto/CreateCustomerRequest.java (Public API của customer feature)
package com.asm.ecommerce.customer.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    private UUID userId;
    private String fullName;
    private String phone;
}
