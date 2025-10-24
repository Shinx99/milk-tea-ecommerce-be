package com.asm.ecommerce.statistics.controller;

import com.asm.ecommerce.statistics.dto.*;
import com.asm.ecommerce.statistics.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final DashboardService dashboardService;

    @Autowired
    public StatisticsController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/revenue/daily")
    public ResponseEntity<List<DailyRevenueStatisticsDTO>> getDailyRevenue(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {

        List<DailyRevenueStatisticsDTO> data = dashboardService.getDailyRevenueStats(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/revenue/weekly")
    public ResponseEntity<List<WeeklyRevenueStatisticsDTO>> getWeeklyRevenue(
            @RequestParam String startWeek,
            @RequestParam String endWeek) {

        List<WeeklyRevenueStatisticsDTO> data = dashboardService.getWeeklyRevenueStats(startWeek, endWeek);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<MonthlyRevenueStatisticsDTO>> getMonthlyRevenue(
            @RequestParam String startMonth,
            @RequestParam String endMonth) {

        List<MonthlyRevenueStatisticsDTO> data = dashboardService.getMonthlyRevenueStats(startMonth, endMonth);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/revenue/yearly")
    public ResponseEntity<List<YearlyRevenueStatisticsDTO>> getYearlyRevenue(
            @RequestParam String startYear,
            @RequestParam String endYear) {

        List<YearlyRevenueStatisticsDTO> data = dashboardService.getYearlyRevenueStats(startYear, endYear);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductStatisticsDTO>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit) {

        List<TopProductStatisticsDTO> data = dashboardService.getTopProducts(limit);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshStatisticsViews() {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mv_revenue_daily");
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mv_revenue_weekly");
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mv_revenue_monthly");
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mv_revenue_yearly");
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mv_top_selling_products");
        return ResponseEntity.ok("Refreshed all statistics views.");
    }
}
