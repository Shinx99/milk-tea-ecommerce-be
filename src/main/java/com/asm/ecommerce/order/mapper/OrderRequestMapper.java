package com.asm.ecommerce.order.mapper;

import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.domain.OrderItem;
import com.asm.ecommerce.order.dto.request.CreateOrderRequestDto;
import com.asm.ecommerce.order.dto.request.OrderItemRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderRequestMapper {

    // DTO item -> entity OrderItem (chưa set order, làm ở service qua order.addItem)
    @Mapping(target = "id",                  ignore = true)
    @Mapping(target = "order",               ignore = true)
    @Mapping(source = "productId",           target = "productId")
    @Mapping(source = "quantity",            target = "quantity")
    @Mapping(source = "sizeCategoryId",      target = "sizeCategoryId")
    @Mapping(source = "sugarCategoryId",     target = "sugarCategoryId")
    @Mapping(source = "iceCategoryId",       target = "iceCategoryId")
    @Mapping(source = "temperatureCategoryId", target = "temperatureCategoryId")
    @Mapping(target = "price",               ignore = true) // tính từ Product
    @Mapping(target = "lineTotal",           ignore = true) // tính trong service
    @Mapping(target = "productName",         ignore = true)
    @Mapping(target = "productImage",        ignore = true)
    @Mapping(target = "note",                ignore = true)
    OrderItem toOrderItem(OrderItemRequestDto dto);

    List<OrderItem> toOrderItems(List<OrderItemRequestDto> dtos);

    // Request -> Order (chỉ map phần cơ bản, số tiền & status xử lý trong service)
    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "status",        ignore = true)
    @Mapping(target = "placedAt",      ignore = true)
    @Mapping(target = "confirmedAt",   ignore = true)
    @Mapping(target = "completedAt",   ignore = true)
    @Mapping(target = "cancelledAt",   ignore = true)
    @Mapping(target = "discountTotal", ignore = true)
    @Mapping(target = "taxTotal",      ignore = true)
    @Mapping(target = "shippingFee",   ignore = true)
    @Mapping(target = "subtotal",      ignore = true)
    @Mapping(target = "total",         ignore = true)
    @Mapping(target = "orderCode",     ignore = true)
    @Mapping(source = "note",          target = "note")
    @Mapping(target = "currency",      ignore = true)
    @Mapping(target = "items",         ignore = true) // set qua order.addItem(...)
    @Mapping(source = "customerId",    target = "customerId")
    @Mapping(source = "customerName",  target = "customerName") // thêm
    @Mapping(source = "phone",         target = "phone")        // thêm
    @Mapping(source = "address",       target = "address")
    Order toOrder(CreateOrderRequestDto dto);
}
