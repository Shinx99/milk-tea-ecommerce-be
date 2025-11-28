package com.asm.ecommerce.cart.repository;

import com.asm.ecommerce.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findByCustomerId(UUID customerId);

    List<Cart> findByCustomerIdAndStatus(UUID customerId, String status);

    Optional<Cart> findByCustomerIdAndProductIdAndStatus(UUID customerId, UUID productId, String status);

    //Solve expires
    List<Cart> findByExpiresAtBeforeAndStatus(Instant expriesAt, String status);

    //Check exists
    boolean existsByCustomerIdAndProductIdAndStatus(UUID customerId, UUID productId, String status);

    // Truy vấn theo từng dòng sản phẩm và option đã chọn
    Optional<Cart> findByCustomerIdAndProductIdAndSizeCategoryIdAndSugarCategoryIdAndIceCategoryIdAndTemperatureCategoryIdAndStatus(
            UUID customerId,
            UUID productId,
            UUID sizeCategoryId,
            UUID sugarCategoryId,
            UUID iceCategoryId,
            UUID temperatureCategoryId,
            String status
    );

    // Kiểm tra tồn tại sản phẩm với options để tránh duplicate dòng cart
    boolean existsByCustomerIdAndProductIdAndSizeCategoryIdAndSugarCategoryIdAndIceCategoryIdAndTemperatureCategoryIdAndStatus(
            UUID customerId,
            UUID productId,
            UUID sizeCategoryId,
            UUID sugarCategoryId,
            UUID iceCategoryId,
            UUID temperatureCategoryId,
            String status
    );


}
