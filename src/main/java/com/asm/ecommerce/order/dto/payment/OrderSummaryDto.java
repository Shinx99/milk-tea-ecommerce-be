package com.asm.ecommerce.order.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderSummaryDto {
    private UUID id;
    private String orderCode;
    private BigDecimal total;
    private String currency;
    private String status;

}