package com.asm.ecommerce.cart.repository;

import com.asm.ecommerce.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findByCustomerId(UUID customerId);

    List<Cart> findByCustomerIdAndStatus(UUID customerId, String status);

    Optional<Cart> findByCustomerIdAndProductIdAndStatus(
            UUID customerId,
            UUID productId,
            String status
    );

    // Solve expires
    List<Cart> findByExpiresAtBeforeAndStatus(
            Instant expiresAt,
            String status

    );

    // Check exists
    boolean existsByCustomerIdAndProductIdAndStatus(
            UUID customerId,
            UUID productId,
            String status
    );

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

    /*@Query("""
SELECT c
FROM Cart c
WHERE c.customerId = :customerId
AND c.productId = :productId
AND c.sizeCategoryId = :sizeCategoryId
AND c.sugarCategoryId = :sugarCategoryId
AND c.iceCategoryId = :iceCategoryId
AND c.temperatureCategoryId = :temperatureCategoryId
AND c.status = :status
""")
    Optional<Cart> findByCustomerIdAndProductIdAndSizeCategoryIdAndSugarCategoryIdAndIceCategoryIdAndTemperatureCategoryIdAndStatus(
            @Param("customerId") UUID customerId,
            @Param("productId") UUID productId,
            @Param("sizeCategoryId") UUID sizeCategoryId,
            @Param("sugarCategoryId") UUID sugarCategoryId,
            @Param("iceCategoryId") UUID iceCategoryId,
            @Param("temperatureCategoryId") UUID temperatureCategoryId,
            @Param("status") Cart.CartStatus status
    );*/

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
