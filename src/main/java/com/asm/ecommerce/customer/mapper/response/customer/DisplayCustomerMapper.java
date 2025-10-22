package com.asm.ecommerce.customer.mapper.response.customer;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisplayCustomerMapper {
    @Mapping(target = "id",        source = "customer.id")
    @Mapping(target = "email",     source = "user.email")
    @Mapping(target = "phone",     source = "customer.phone")
    @Mapping(target = "fullname",  source = "customer.fullname")
    @Mapping(target = "active",    source = "customer.active")
    @Mapping(target = "createdAt",  source = "customer.createdAt")
    @Mapping(target = "updatedAt", source = "customer.updatedAt")
    DisplayAdminCustomerResponse display(Customer customer, UserDto user);
}
