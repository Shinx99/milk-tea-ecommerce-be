package com.asm.ecommerce.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductStatisticsDTO {
    private String productId;
    private String productName;
    private Integer totalQuantitySold;
    private BigDecimal totalRevenue;
    // ...
}
