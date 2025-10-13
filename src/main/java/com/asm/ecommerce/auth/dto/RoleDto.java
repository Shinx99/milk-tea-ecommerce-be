package com.asm.ecommerce.auth.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private UUID id;
    private String role;
    private String description;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
