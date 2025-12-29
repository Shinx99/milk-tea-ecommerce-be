package com.asm.ecommerce.customer.dto.request.customer;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UpdateAdminCustomerRequest {

    @Setter
    @Getter
    @Size(max = 40)
    private String phone;

    @Getter
    @Setter
    @Size(max = 255)
    private String fullname;

    @Getter
    @Setter
    private Boolean active;
}
