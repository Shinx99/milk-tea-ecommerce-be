package com.asm.ecommerce.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class CartItemResponseDto {

    private UUID id;
    private UUID productId;

    private String productName;
    private String productImage;
    private BigDecimal unitPrice;

    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;

    // FIX 1: Dùng ISO_INSTANT (chuẩn quốc tế)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant addedAt;

    // FIX 2: Hoặc dùng ISO 8601 format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    // Các trường thuộc tính option
    private UUID sizeCategoryId;
    private String sizeName;
    private UUID sugarCategoryId;
    private String sugarName;
    private UUID iceCategoryId;
    private String iceName;
    private UUID temperatureCategoryId;
    private String temperatureName;

    /**
     * Kiểm tra hết hàng: quantity <= 0
     */
    public Boolean getIsOutOfStock() {
        return quantity != null && quantity <= 0;
    }

    /**
     * Kiểm tra sắp hết hàng: quantity < 5
     */
    public Boolean getIsLowStock() {
        return quantity != null && quantity > 0 && quantity < 5;
    }

    /**
     * Kiểm tra có thể tăng số lượng: quantity < getMaxAllowedQuantity()
     */
    public Boolean getCanIncreaseQuantity() {
        return quantity < getMaxAllowedQuantity();
    }

    /**
     * Số lượng tối đa cho phép (có thể config)
     */
    public Integer getMaxAllowedQuantity() {
        return 99; // Giới hạn mềm để tránh spam
    }

    /**
     * Tính tổng tiền tự động
     */
    public BigDecimal getTotalPrice() {
        if (unitPrice != null && quantity != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Trạng thái hiển thị cho UI
     */
    public String getStockStatus() {
        if (getIsOutOfStock()) {
            return "Hết hàng";
        } else if (getIsLowStock()) {
            return "Sắp hết hàng";
        } else {
            return "Còn hàng";
        }
    }

}
