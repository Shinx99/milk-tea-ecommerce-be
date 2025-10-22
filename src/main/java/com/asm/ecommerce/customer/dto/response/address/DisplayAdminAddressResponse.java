package com.asm.ecommerce.customer.dto.response.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //Loai cac truong null ra khoi JSON response -> reponse nhe hon
public class DisplayAdminAddressResponse {

    //Customer
    private UUID customer;

    @Size(max = 255)
    private String fullname;

    //Customer
    @Size(max = 40)
    private String phone;

    //Address-------------------
    private UUID id;

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
