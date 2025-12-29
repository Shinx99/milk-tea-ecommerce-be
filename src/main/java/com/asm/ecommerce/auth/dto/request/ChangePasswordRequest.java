package com.asm.ecommerce.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "New password must be between 8-100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character (@$!%*?&#)"
    )
    private String newPassword;
    /*
            (?=.*[a-z]) - Ít nhất 1 chữ cái thường

            (?=.*[A-Z]) - Ít nhất 1 chữ cái hoa

            (?=.*\\d) - Ít nhất 1 số

            (?=.*[@$!%*?&#]) - Ít nhất 1 ký tự đặc biệt

            [A-Za-z\\d@$!%*?&#]{8,} - Độ dài tối thiểu 8 ký tự, chỉ chấp nhận các ký tự này
     */


    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
