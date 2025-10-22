package com.asm.ecommerce.customer.dto.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SearchCustomerRequest {
    @Size(max = 255)
    private String word;     //chuoi ky tu nguoi dung nhap vao de tim kiem

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 40)
    private String phone;

    @Size(max = 255)
    private String fullname;



}
