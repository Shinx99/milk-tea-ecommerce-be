package com.asm.ecommerce.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequestDto {

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private Integer quantity;

    // Các thuộc tính option
    private UUID sizeCategoryId;
    private UUID sugarCategoryId;
    private UUID iceCategoryId;
    private UUID temperatureCategoryId;
}
