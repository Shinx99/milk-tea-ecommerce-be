package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.TopProductStatisticsDTO;
import java.util.List;

public interface TopProductStatisticsRepository {
    List<TopProductStatisticsDTO> findTopSellingProducts(int limit);
}
