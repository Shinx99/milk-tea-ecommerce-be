package com.asm.ecommerce.home.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeProductResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String image;
}
