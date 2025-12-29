package com.asm.ecommerce.customer.mapper.response.address;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisplayAddressMapper {
    @Mapping(target = "customer", source = "customer.id")
    @Mapping(target = "fullname", source = "customer.fullname")
    @Mapping(target = "phone", source = "customer.phone")
    @Mapping(target = "id", source = "address.id")
    @Mapping(target = "number", source = "address.number")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "ward", source = "address.ward")
    @Mapping(target = "district", source = "address.district")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "isDefault", source = "address.isDefault")
    @Mapping(target = "active", source = "address.active")
    DisplayAdminAddressResponse display(Customer customer, Address address);
}
