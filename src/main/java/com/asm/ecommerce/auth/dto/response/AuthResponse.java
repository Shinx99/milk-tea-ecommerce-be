package com.asm.ecommerce.auth.dto.response;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UUID userId;
    private String email;
    private String token;
    private String tokenType;
    private Long expiresIn;
    private UUID roleId;
    private UUID customerId;
    private String roleName;
}
