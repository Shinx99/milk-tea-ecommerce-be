package com.asm.ecommerce.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentResultDto {

    private UUID orderId;
    private UUID paymentId;
    private String orderCode;
    private String provider;       // VNPAY / COD...
    private String paymentStatus;  // PENDING / SUCCESS / FAILED / REFUNDED
    private BigDecimal amount;
    private String currency;

    private String vnpTransactionNo;
    private String vnpBankCode;
    private String vnpPayDate;
    private String vnpResponseCode;

    private String message;

}
// todo: PaymentResultDto (backend → FE khi xem trạng thái thanh toán / màn payment-result)