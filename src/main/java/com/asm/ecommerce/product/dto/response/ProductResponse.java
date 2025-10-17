package com.asm.ecommerce.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DỮ LIỆU BE GỬI LÊN FE
public class ProductResponse {
    private UUID id;
    private UUID categoryId;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
