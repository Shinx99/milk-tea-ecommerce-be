package com.asm.ecommerce.order.repository;

import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    // Lấy full item của 1 đơn
    List<OrderItem> findByOrder(Order order);

    // Dùng id theo đơn (cách 2)
    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByProductId(UUID productId);



}
