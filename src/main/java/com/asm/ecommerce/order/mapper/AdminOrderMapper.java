package com.asm.ecommerce.order.mapper;

import com.asm.ecommerce.auth.dto.response.AuthResponse;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.dto.response.AdminOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//todo: Vuong -> ADMIN_ORDER

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface AdminOrderMapper {

    @Mapping(source = "order.id",         target = "id")
    @Mapping(source = "order.orderCode",  target = "orderCode")
    @Mapping(source = "order.customerId", target = "customerId")

    @Mapping(source = "customer.email",    target = "email")
    @Mapping(source = "customer.fullname", target = "fullname")
    @Mapping(source = "customer.phone",    target = "phone")

    @Mapping(target = "address",
            expression = "java(buildFullAddress(address))")

    @Mapping(source = "order.status",        target = "status")
    @Mapping(source = "order.placedAt",      target = "placedAt")
    @Mapping(source = "order.subtotal",      target = "subtotal")
    @Mapping(source = "order.discountTotal", target = "discountTotal")
    @Mapping(source = "order.taxTotal",      target = "taxTotal")
    @Mapping(source = "order.shippingFee",   target = "shippingFee")
    @Mapping(source = "order.total",         target = "total")
    @Mapping(source = "order.currency",      target = "currency")
    @Mapping(source = "order.note",          target = "note")
    @Mapping(source = "order.createdAt",     target = "createdAt")
    @Mapping(source = "order.updatedAt",     target = "updatedAt")
    @Mapping(source = "order.items",         target = "items")
    AdminOrderDto toAdminOrderDto(
            Order order,
            DisplayAdminCustomerResponse customer,
            DisplayAdminAddressResponse address);

    default String buildFullAddress(DisplayAdminAddressResponse source) {
        if (source == null) return null;

        StringBuilder sb = new StringBuilder();
        if (source.getNumber()   != null) sb.append(source.getNumber()).append(" ");
        if (source.getStreet()   != null) sb.append(source.getStreet()).append(", ");
        if (source.getWard()     != null) sb.append(source.getWard()).append(", ");
        if (source.getDistrict() != null) sb.append(source.getDistrict()).append(", ");
        if (source.getCity()     != null) sb.append(source.getCity()).append(", ");
        if (source.getProvince() != null) sb.append(source.getProvince()).append(", ");
        if (source.getCountry()  != null) sb.append(source.getCountry()).append(".");

        return sb.toString().replaceAll(", $", "");
    }
}

