package com.asm.ecommerce.order.repository;

import com.asm.ecommerce.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
