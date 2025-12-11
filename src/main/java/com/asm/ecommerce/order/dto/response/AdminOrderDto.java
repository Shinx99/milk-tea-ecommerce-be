package com.asm.ecommerce.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

//todo: Vuong -> ADMIN_ORDER

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminOrderDto {

    private UUID id;
    private String orderCode;
    private UUID customerId;

    //Phan Customer
    private String email;
    private String fullname;
    private String phone;
    private String address;

    private String status;
    private Instant placedAt;
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private String currency;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderItemDto> items;

}
