package com.asm.ecommerce.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {

    private UUID id;
    private UUID productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal lineTotal;
    private UUID sizeCategoryId;
    private UUID sugarCategoryId;
    private UUID iceCategoryId;
    private UUID temperatureCategoryId;
}
