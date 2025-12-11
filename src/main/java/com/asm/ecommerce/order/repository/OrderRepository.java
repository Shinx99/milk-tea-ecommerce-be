package com.asm.ecommerce.order.repository;

import com.asm.ecommerce.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByCustomerId(UUID customerId);

    // tìm theo mã đơn (order Code)
    Optional<Order> findByOrderCode(String orderCode);

    // tìm theo customer
    List<Order> findByCustomerIdOrderByPlacedAtDesc(UUID customerId);

    // lọc theo customer + status
    List<Order> findByCustomerIdAndStatusOrderByPlacedAtDesc(UUID customerId, String status);

    // Lịch sử đơn của 1 customer (dùng với PageResponse)
    Page<Order> findByCustomerIdOrderByPlacedAtDesc(UUID customerId, Pageable pageable);

    // tìm theo mã đơn
    //Optional<Order> findByOrderCode(String orderCode);

    //todo: Vuong -> ADMIN_ORDER

    //Ham findAll + Search + Phan trang
    @Query(value = """
SELECT o.* FROM orders o
LEFT JOIN customers c ON o.customer_id = c.id
LEFT JOIN users u ON u.id = c.user_id
WHERE 
    (:search IS NULL OR :search = ''
     OR LOWER(c.fullname) ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(u.email)    ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(o.order_code) ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(o.status)     ILIKE '%' || LOWER(:search) || '%')
ORDER BY o.placed_at DESC
""",
            countQuery = """
SELECT COUNT(o.id) FROM orders o
LEFT JOIN customers c ON o.customer_id = c.id
LEFT JOIN users u ON u.id = c.user_id
WHERE 
    (:search IS NULL OR :search = ''
     OR LOWER(c.fullname) ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(u.email)    ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(o.order_code) ILIKE '%' || LOWER(:search) || '%'
     OR LOWER(o.status)     ILIKE '%' || LOWER(:search) || '%')
""",
            nativeQuery = true)
    Page<Order> findOrderAdmin(@Param("search") String search, Pageable pageable);




    //Ham cho Order Detail



}
