package com.asm.ecommerce.statistics.service;

import com.asm.ecommerce.statistics.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DashboardService {

    List<DailyRevenueStatisticsDTO> getDailyRevenueStats(String startDate, String endDate);
    List<WeeklyRevenueStatisticsDTO> getWeeklyRevenueStats(String startWeek, String endWeek);
    List<MonthlyRevenueStatisticsDTO> getMonthlyRevenueStats(String startMonth, String endMonth);
    List<YearlyRevenueStatisticsDTO> getYearlyRevenueStats(String startYear, String endYear);

    List<TopProductStatisticsDTO> getTopProducts(int limit);
    // Thêm các hàm khác nếu cần: đơn hủy, khách hàng...
}
