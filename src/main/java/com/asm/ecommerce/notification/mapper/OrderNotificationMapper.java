package com.asm.ecommerce.notification.mapper;

import com.asm.ecommerce.notification.dto.OrderNotificationDto;
import com.asm.ecommerce.order.dto.response.AdminOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderNotificationMapper {

    @Mapping(source = "id",        target = "id")
    @Mapping(source = "orderCode", target = "orderCode")
    @Mapping(source = "total",     target = "total")
    @Mapping(source = "status",    target = "status")
    @Mapping(source = "placedAt",  target = "placedAt")
    @Mapping(target = "type",
            expression = "java(determineType(order))")
    OrderNotificationDto toDto(AdminOrderDto order);

    default OrderNotificationDto.NotificationType determineType(AdminOrderDto order) {
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            return OrderNotificationDto.NotificationType.ORDER_PAID;
        }
        return OrderNotificationDto.NotificationType.ORDER_COMPlETE;
    }
}
