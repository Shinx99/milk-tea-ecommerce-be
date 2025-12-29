package com.asm.ecommerce.cart.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto {
    
    private UUID id;
    private String name;
    private BigDecimal price;        // QUAN TRỌNG: để tính tiền
    private Integer quantity;        // QUAN TRỌNG: để check stock
    private Boolean isActive;        // QUAN TRỌNG: để check product có available không

    // Thêm để hiển thị ảnh trong cart
    private String imageUrl; // Ảnh đầu tiên (primary image)

    /**
     * Kiểm tra hết hàng
     */
    public Boolean getIsOutOfStock() {
        return quantity != null && quantity <= 0;
    }

    /**
     * Kiểm tra sắp hết hàng
     */
    public Boolean getIsLowStock() {
        return quantity != null && quantity > 0 && quantity < 5;
    }

    /**
     * Kiểm tra có thể thêm vào giỏ với số lượng yêu cầu
     */
    public Boolean canAddToCart(Integer requestedQuantity) {
        return Boolean.TRUE.equals(isActive)
                && quantity != null
                && requestedQuantity != null
                && quantity >= requestedQuantity;
    }
}