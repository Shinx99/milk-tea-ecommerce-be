package com.asm.ecommerce.cart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@Entity
public class Cart {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    // Các thuộc tính option
    @Column(name = "size_category_id")
    private UUID sizeCategoryId;

    @Column(name = "sugar_category_id")
    private UUID sugarCategoryId;

    @Column(name = "ice_category_id")
    private UUID iceCategoryId;

    @Column(name = "temperature_category_id")
    private UUID temperatureCategoryId;


    // Enum cho các trạng thái giỏ hàng
    public enum CartStatus {
        ACTIVE,
        SAVED_FOR_LATER,
        ORDERED,
        REMOVED,
        EXPIRED;

        public String getValue(){
            return this.name();
        }
    }
}


