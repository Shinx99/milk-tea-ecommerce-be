package com.asm.ecommerce.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentResponseDto {

    private UUID orderId;
    private UUID paymentId;
    private String orderCode;
    private String transactionRef; // vnp_TxnRef
    private String paymentUrl;     // URL redirect tới VNPAY
    private String status;         // PENDING / FAILED
    private BigDecimal amount;
    private String currency;
    private String message;

}

// todo: CreatePaymentResponseDto (backend → FE sau khi tạo URL VNPAY)