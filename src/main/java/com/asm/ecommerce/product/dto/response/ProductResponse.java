package com.asm.ecommerce.product.dto.response;

import com.asm.ecommerce.product.domain.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID id;
    private UUID categoryId;
    private String name;
    private String description;
    private Integer quantity;
    private Boolean active;
    private BigDecimal price;
    private List<String> imageUrl;

    // Day la cai list chua [Tra sua, Banh, Kem,...]
    private CategoryResponse category;

    // Đây là cái list chứa [Size, Đá, Đường...]
    private List<OptionGroupDTO> options;

    //todo: ====== Cart ========
    public String getPrimaryImage(){
        return (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl.get(0) : null;
    }

    public Boolean isInStock(){
        return quantity != null && quantity >0;
    }

    public Boolean  isLowStock(){
        return quantity != null && quantity > 0 && quantity < 10;
    }
}