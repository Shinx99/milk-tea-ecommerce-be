package com.asm.ecommerce.customer.mapper.response;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisplayMapper {
    @Mapping(target = "id",        source = "customer.id")
    @Mapping(target = "email",     source = "user.email")
    @Mapping(target = "phone",     source = "customer.phone")
    @Mapping(target = "fullname",  source = "customer.fullname")
    @Mapping(target = "active",    source = "customer.isActive")
    @Mapping(target = "createdAt",  source = "customer.createdAt")
    @Mapping(target = "updatedAt", source = "customer.updatedAt")
    DisplayResponse display(Customer customer, UserDto user);
}
