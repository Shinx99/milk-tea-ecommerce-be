package com.asm.ecommerce.customer.mapper.request;

import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.UpdateProfileRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateProfileMapper {

    @BeanMapping(ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Hibernate tự cập nhật @UpdateTimestamp
    void updateProfileCustomer(@MappingTarget Customer entity, UpdateProfileRequest dto);
}
