package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.YearlyRevenueStatisticsDTO;
import java.util.List;

public interface YearlyRevenueStatisticsRepository {
    List<YearlyRevenueStatisticsDTO> findByPeriodYearRange(String startYear, String endYear);
}
