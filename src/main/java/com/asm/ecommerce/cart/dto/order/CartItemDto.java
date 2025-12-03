package com.asm.ecommerce.cart.dto.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private UUID id;              // id bản ghi carts
    private UUID customerId;
    private UUID productId;
    private String productName;
    private String productImage;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;   // unitPrice * quantity

    private UUID sizeCategoryId;
    private UUID sugarCategoryId;
    private UUID iceCategoryId;
    private UUID temperatureCategoryId;
}
