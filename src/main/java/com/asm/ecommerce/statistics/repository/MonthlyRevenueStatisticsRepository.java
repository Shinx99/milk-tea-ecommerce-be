package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.MonthlyRevenueStatisticsDTO;
import java.util.List;

public interface MonthlyRevenueStatisticsRepository {
    List<MonthlyRevenueStatisticsDTO> findByPeriodMonthRange(String startMonth, String endMonth);
}
