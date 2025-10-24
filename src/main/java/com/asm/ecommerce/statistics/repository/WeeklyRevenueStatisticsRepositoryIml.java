package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.WeeklyRevenueStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeeklyRevenueStatisticsRepositoryIml implements WeeklyRevenueStatisticsRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<WeeklyRevenueStatisticsDTO> findByPeriodWeekRange(String startWeek, String endWeek) {
        String sql = "SELECT period_week, category_name, total_revenue, total_quantity, max_price, min_price, avg_price " +
                "FROM mv_revenue_weekly " +
                "WHERE period_week BETWEEN ? AND ?";

        return jdbcTemplate.query(sql,
                new Object[]{startWeek, endWeek},
                new BeanPropertyRowMapper<>(WeeklyRevenueStatisticsDTO.class));
    }
}
