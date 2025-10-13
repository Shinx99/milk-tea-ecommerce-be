package com.asm.ecommerce.customer.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateAdminRequest {

    @Size(max = 40)
    private String phone;

    @Size(max = 255)
    private String fullname;

    private boolean isActive;
}
