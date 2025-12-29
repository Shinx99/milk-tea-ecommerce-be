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
public class YearlyRevenueStatisticsDTO {
    private String periodYear;
    private String categoryName;
    private BigDecimal totalRevenue;
    private Integer totalQuantity;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private BigDecimal avgPrice;
    // ...
}
