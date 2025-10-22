package com.asm.ecommerce.customer.mapper.request.address;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UpdateAdminAddressMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminAddress(@MappingTarget Address entity, UpdateAdminAddressRequest dto);
}
