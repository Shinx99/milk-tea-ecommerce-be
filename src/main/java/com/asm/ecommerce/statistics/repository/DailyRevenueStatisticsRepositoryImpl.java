package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.DailyRevenueStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyRevenueStatisticsRepositoryImpl implements DailyRevenueStatisticsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<DailyRevenueStatisticsDTO> findByPeriodDayRange(String startDate, String endDate) {
        String sql = "SELECT period_day, category_name, total_revenue, total_quantity, max_price, min_price, avg_price " +
                "FROM mv_revenue_daily " +
                "WHERE period_day BETWEEN ? AND ?";

        return jdbcTemplate.query(sql,
                new Object[]{startDate, endDate},
                new BeanPropertyRowMapper<>(DailyRevenueStatisticsDTO.class));
    }
}

