package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.MonthlyRevenueStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MonthlyRevenueStatisticsRepositoryIml implements MonthlyRevenueStatisticsRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MonthlyRevenueStatisticsDTO> findByPeriodMonthRange(String startMonth, String endMonth) {
        String sql = "SELECT period_month, category_name, total_revenue, total_quantity, max_price, min_price, avg_price " +
                "FROM mv_revenue_monthly " +
                "WHERE period_month BETWEEN ? AND ?";

        return jdbcTemplate.query(sql,
                new Object[]{startMonth, endMonth},
                new BeanPropertyRowMapper<>(MonthlyRevenueStatisticsDTO.class));
    }
}
