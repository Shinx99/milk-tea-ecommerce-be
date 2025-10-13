package com.asm.ecommerce.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SearchRequest {
    @Size(max = 255)
    private String word;     //chuoi ky tu nguoi dung nhap vao de tim kiem

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 40)
    private String phone;

    @Size(max = 255)
    private String fullname;

    // sort theo whitelist cột, định dạng: field,dir (không khoảng trắng)
    @Pattern(regexp = "^(email|fullname|createdAt|updatedAt)(,(asc|desc))?$")
    private String sort = "createdAt,desc";

}
