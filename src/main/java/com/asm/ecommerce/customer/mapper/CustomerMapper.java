package com.asm.ecommerce.customer.mapper;

import com.asm.ecommerce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/*
* Register
*
* */

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Map từ các tham số (fullName camelCase)
    // sang entity Customer (fullname lowercase)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "fullname", source = "fullName")
    @Mapping(target = "phone", source = "phone")
    Customer toEntity(UUID userId, String fullName, String phone);
}
