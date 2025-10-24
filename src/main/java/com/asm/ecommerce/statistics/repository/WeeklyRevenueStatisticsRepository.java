package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.WeeklyRevenueStatisticsDTO;
import java.util.List;

public interface WeeklyRevenueStatisticsRepository {
    List<WeeklyRevenueStatisticsDTO> findByPeriodWeekRange(String startWeek, String endWeek);
}
