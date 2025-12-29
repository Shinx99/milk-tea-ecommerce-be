package com.asm.ecommerce.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentRequestDto {

    private UUID orderId;
    private BigDecimal amount;
    private String description; // sẽ map vào vnp_OrderInfo
    private String provider;    // "VNPAY" (optional, backend có thể tự set)

}

//todo: CreatePaymentRequestDto (FE → backend khi chọn VNPAY)