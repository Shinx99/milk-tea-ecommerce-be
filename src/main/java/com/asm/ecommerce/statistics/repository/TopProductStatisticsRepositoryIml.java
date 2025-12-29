package com.asm.ecommerce.statistics.repository;

import com.asm.ecommerce.statistics.dto.MonthlyRevenueStatisticsDTO;
import com.asm.ecommerce.statistics.dto.TopProductStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopProductStatisticsRepositoryIml implements TopProductStatisticsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TopProductStatisticsDTO> findTopSellingProducts(int limit) {
        String sql = "SELECT product_id, product_name, total_quantity_sold, total_revenue " +
                "FROM mv_top_selling_products " +
                "ORDER BY total_quantity_sold DESC LIMIT ?";
        return jdbcTemplate.query(sql,
                new Object[]{limit},
                new BeanPropertyRowMapper<>(TopProductStatisticsDTO.class));
    }
}
