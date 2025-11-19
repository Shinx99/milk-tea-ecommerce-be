package com.asm.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Ghi chú: DỮ LIỆU FE GỬI CHO BE
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank   (message = "CategoryName not found")
    private String categoryName;

    // Ghi chú: ID của danh mục cha (có thể là null nếu là danh mục gốc).
    private UUID parentId;

    private Integer sortOrder;
    private Boolean isActive = true;
}