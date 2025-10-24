package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.YearlyRevenueStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class YearlyRevenueStatisticsRepositoryIml implements YearlyRevenueStatisticsRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<YearlyRevenueStatisticsDTO> findByPeriodYearRange(String startYear, String endYear) {
        String sql = "SELECT period_year, category_name, total_revenue, total_quantity, max_price, min_price, avg_price " +
                "FROM mv_revenue_yearly " +
                "WHERE period_year BETWEEN ? AND ?";

        return jdbcTemplate.query(sql,
                new Object[]{startYear, endYear},
                new BeanPropertyRowMapper<>(YearlyRevenueStatisticsDTO.class));
    }
}
