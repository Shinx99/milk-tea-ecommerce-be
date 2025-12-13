package com.asm.ecommerce.order.mapper;

import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.domain.OrderItem;
import com.asm.ecommerce.order.dto.invoice.InvoiceDataDto;
import com.asm.ecommerce.order.dto.invoice.InvoiceItemDto;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    // Order -> InvoiceDataDto (map các trường chung)
    @Mapping(source = "id",           target = "orderId")
    @Mapping(source = "customerName", target = "customerName")
    @Mapping(source = "phone",        target = "phone")
    @Mapping(source = "address",      target = "address")
    @Mapping(target = "items",        ignore = true)
    InvoiceDataDto toInvoiceData(Order order);

    // OrderItem -> InvoiceItemDto (không có STT, sẽ set ở service)
    @Mapping(source = "price",       target = "unitPrice")
    @Mapping(target = "lineTotal",
             expression = "java(calcLineTotal(orderItem))")
    @Mapping(target = "stt",         ignore = true)
    InvoiceItemDto toInvoiceItemDto(OrderItem orderItem);

    List<InvoiceItemDto> toInvoiceItemDtos(List<OrderItem> orderItems);

    default BigDecimal calcLineTotal(OrderItem item) {
        if (item.getPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
