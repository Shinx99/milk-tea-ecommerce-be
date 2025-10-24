package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.DailyRevenueStatisticsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRevenueStatisticsRepository {
    List<DailyRevenueStatisticsDTO> findByPeriodDayRange(String startDate, String endDate);
}
