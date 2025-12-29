package com.asm.ecommerce.auth.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String email;
    private Boolean isActive;
    private Instant createdAt;
    private UUID roleId;
    private String roleName;
}
