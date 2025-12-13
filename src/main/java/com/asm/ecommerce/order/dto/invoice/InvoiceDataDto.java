package com.asm.ecommerce.order.dto.invoice;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDataDto {
    
    private UUID orderId;
    private String orderCode;
    private Instant placedAt;
    
    // Customer info
    private String customerName;
    private String customerEmail;
    private String phone;
    private String address;
    
    // Order items
    private List<InvoiceItemDto> items;
    
    // Totals
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private String currency;
}