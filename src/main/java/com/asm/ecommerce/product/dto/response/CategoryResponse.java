package com.asm.ecommerce.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List; // Nhớ import List
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private UUID id;
    private String categoryName;
    private UUID parentId;
    private Boolean isActive;
    private Integer sortOrder;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // --- ĐÂY LÀ DÒNG QUAN TRỌNG NHẤT BẠN CẦN THÊM ---
    // Biến này dùng để chứa danh sách con của nó (Đệ quy)
    private List<CategoryResponse> children;
}
