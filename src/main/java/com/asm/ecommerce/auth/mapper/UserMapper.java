package com.asm.ecommerce.auth.mapper;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.dto.response.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleName",
            expression = "java(user.getRole() != null ? user.getRole().getRole() : null)")
    UserDto toDto(User user);

/*    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(UserDto userDto);*/

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expiresIn", constant = "86400L")
    @Mapping(target = "roleName",
            expression = "java(user.getRole() != null ? user.getRole().getRole() : null)")
    AuthResponse toAuthResponse(User user, String token);
}


