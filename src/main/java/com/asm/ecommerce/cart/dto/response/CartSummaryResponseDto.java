package com.asm.ecommerce.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponseDto {

    private List<CartItemResponseDto> activeItems;
    private List<CartItemResponseDto> saveForLaterItems;
    private Integer totalActiveItems;
    private Integer totalSaveItems;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private String currency;

}
