package com.asm.ecommerce.order.mapper;

import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.domain.OrderItem;
import com.asm.ecommerce.order.dto.payment.OrderSummaryDto;
import com.asm.ecommerce.order.dto.response.OrderDetailDto;
import com.asm.ecommerce.order.dto.response.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // OrderItem -> OrderItemDto
    @Mapping(source = "productId",          target = "productId")
    @Mapping(source = "productName",        target = "productName")
    @Mapping(source = "productImage",       target = "productImage")
    @Mapping(source = "quantity",           target = "quantity")
    @Mapping(source = "price",              target = "price")
    @Mapping(source = "lineTotal",          target = "lineTotal")
    @Mapping(source = "sizeCategoryId",     target = "sizeCategoryId")
    @Mapping(source = "sugarCategoryId",    target = "sugarCategoryId")
    @Mapping(source = "iceCategoryId",      target = "iceCategoryId")
    @Mapping(source = "temperatureCategoryId", target = "temperatureCategoryId")
    OrderItemDto toOrderItemDto(OrderItem entity);

    List<OrderItemDto> toOrderItemDtos(List<OrderItem> entities);

    // Order -> OrderDetailDto
    @Mapping(source = "id",            target = "id")
    @Mapping(source = "orderCode",     target = "orderCode")
    @Mapping(source = "status",        target = "status")
    @Mapping(source = "placedAt",      target = "placedAt")
    @Mapping(source = "subtotal",      target = "subtotal")
    @Mapping(source = "discountTotal", target = "discountTotal")
    @Mapping(source = "taxTotal",      target = "taxTotal")
    @Mapping(source = "shippingFee",   target = "shippingFee")
    @Mapping(source = "total",         target = "total")
    @Mapping(source = "currency",      target = "currency")
    @Mapping(source = "note",          target = "note")
    @Mapping(source = "items",         target = "items")
    OrderDetailDto toOrderDetailDto(Order entity);


    // todo: ===== Payment ====
    @Mapping(source = "id",        target = "id")
    @Mapping(source = "orderCode", target = "orderCode")
    @Mapping(source = "total",     target = "total")
    @Mapping(source = "currency",  target = "currency")
    @Mapping(source = "status",    target = "status")
    OrderSummaryDto toOrderSummaryDto(Order entity);

}
