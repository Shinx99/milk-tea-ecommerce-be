package com.asm.ecommerce.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotNull(message = "CategoryId is required")
    private UUID categoryId;

    @NotBlank()
    @Size(max = 100)
    private String name;

    private String description;
    @NotNull @Min(0)
    private Integer quantity;

    @NotNull @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    @NotNull
    private Boolean active;

    private List<String> imageUrl; // Tên trường khớp với FE
}