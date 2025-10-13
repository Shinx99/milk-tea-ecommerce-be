package com.asm.ecommerce.customer.mapper.request;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.request.UpdateProfileRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateProfileMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Hibernate tự cập nhật @UpdateTimestamp
    void updateCustomer(@MappingTarget CustomerModel entity, UpdateProfileRequest dto);
}
