package com.asm.ecommerce.customer.mapper.request;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.request.UpdateAdminRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateAdminMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAdmin(@MappingTarget CustomerModel entity, UpdateAdminRequest dto);

}
