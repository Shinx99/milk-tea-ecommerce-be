package com.asm.ecommerce.customer.mapper.request;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.request.UpdateAdminRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateAdminMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateAdmin(@MappingTarget CustomerModel entity, UpdateAdminRequest dto);

}
