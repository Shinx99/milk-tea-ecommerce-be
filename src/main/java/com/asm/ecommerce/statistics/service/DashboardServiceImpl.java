package com.asm.ecommerce.statistics.service;

import com.asm.ecommerce.statistics.dto.*;
import com.asm.ecommerce.statistics.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService{

    private final DailyRevenueStatisticsRepository dailyRepo;
    private final WeeklyRevenueStatisticsRepositoryIml weeklyRepo;
    private final MonthlyRevenueStatisticsRepositoryIml monthlyRepo;
    private final YearlyRevenueStatisticsRepositoryIml yearlyRepo;
    private final TopProductStatisticsRepositoryIml topProductRepo;

    @Autowired
    public DashboardServiceImpl(DailyRevenueStatisticsRepository dailyRepo,
                                WeeklyRevenueStatisticsRepositoryIml weeklyRepo,
                                MonthlyRevenueStatisticsRepositoryIml monthlyRepo,
                                YearlyRevenueStatisticsRepositoryIml yearlyRepo,
                                TopProductStatisticsRepositoryIml topProductRepo) {
        this.dailyRepo = dailyRepo;
        this.weeklyRepo = weeklyRepo;
        this.monthlyRepo = monthlyRepo;
        this.yearlyRepo = yearlyRepo;
        this.topProductRepo = topProductRepo;
    }

    @Override
    public List<DailyRevenueStatisticsDTO> getDailyRevenueStats(String startDate, String endDate) {
        return dailyRepo.findByPeriodDayRange(startDate, endDate);
    }

    @Override
    public List<WeeklyRevenueStatisticsDTO> getWeeklyRevenueStats(String startWeek, String endWeek) {
        return weeklyRepo.findByPeriodWeekRange(startWeek, endWeek);
    }

    @Override
    public List<MonthlyRevenueStatisticsDTO> getMonthlyRevenueStats(String startMonth, String endMonth) {
        return monthlyRepo.findByPeriodMonthRange(startMonth, endMonth);
    }

    @Override
    public List<YearlyRevenueStatisticsDTO> getYearlyRevenueStats(String startYear, String endYear) {
        return yearlyRepo.findByPeriodYearRange(startYear, endYear);
    }

    @Override
    public List<TopProductStatisticsDTO> getTopProducts(int limit) {
        return topProductRepo.findTopSellingProducts(limit);
    }
}
