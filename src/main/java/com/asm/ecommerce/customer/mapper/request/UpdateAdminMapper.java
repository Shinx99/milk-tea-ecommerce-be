package com.asm.ecommerce.customer.mapper.request;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.request.UpdateAdminRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateAdminMapper {

    @BeanMapping(ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Hibernate tự cập nhật @UpdateTimestamp
    void updateAdminCustomer(@MappingTarget CustomerModel entity, UpdateAdminRequest dto);
}
