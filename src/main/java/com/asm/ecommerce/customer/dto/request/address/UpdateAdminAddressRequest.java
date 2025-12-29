package com.asm.ecommerce.customer.dto.request.address;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAdminAddressRequest {

    //Address-------------------
    @Size(max = 50)
    private String number;

    @Size(max = 100)
    private String street;

    @Size(max = 100)
    private String ward;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @Size(max = 100)
    private String country;

    private Boolean isDefault;

    private Boolean active;
}
