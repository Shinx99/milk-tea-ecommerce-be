package com.asm.ecommerce.auth.mapper;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.dto.request.RegisterRequest;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ========== User → UserDto ==========
    @Mapping(target = "roleName",
            expression = "java(user.getRole() != null ? user.getRole().getRole() : null)")
    UserDto toDto(User user);

    // ========== RegisterRequest → User Entity ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "passwordHash", source = "encodedPassword")
    @Mapping(target = "roleId", source = "roleId")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toEntity(RegisterRequest request, UUID roleId, String encodedPassword);

    // ========== User → AuthResponse (for Register/Login) ==========
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expiresIn", constant = "86400L")
    @Mapping(target = "roleName",
            expression = "java(user.getRole() != null ? user.getRole().getRole() : null)")
    AuthResponse toAuthResponse(User user, String token);
}


