package com.asm.ecommerce.order.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemRequestDto {

    private UUID productId;
    private Integer quantity;
    private UUID sizeCategoryId;
    private UUID sugarCategoryId;
    private UUID iceCategoryId;
    private UUID temperatureCategoryId;

}
