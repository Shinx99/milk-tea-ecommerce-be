package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public class CategoryMapper {
    private CategoryMapper() {}

    // Cho ca 1 list
    public static List<CategoryResponse> toResponse(List<ProductCategory> categories){
        if(categories == null) return List.of();
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    // Ghi chú: Chuyển đổi Entity sang Response DTO. (Cho 1 Object thoi)
    public static CategoryResponse toResponse (ProductCategory category){
        if(category == null) return null;
        UUID parentId = category.getParent() != null
                ? category.getParent().getId() : null;

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .parentId(parentId)
                .sortOrder(category.getSortOrder())
                .isActive(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    // Ghi chú: Cập nhật Entity hiện có bằng dữ liệu từ Request DTO.
    public static void UpdateEntity (ProductCategory entity, CategoryRequest req, ProductCategory parent){
        if(entity == null || req == null) return;

        // Ghi chú: Chỉ cập nhật trường nếu dữ liệu mới được cung cấp (không null).
        if(req.getCategoryName() != null) entity.setCategoryName(req.getCategoryName());
        if(req.getIsActive() !=null) entity.setActive(req.getIsActive());
        if(req.getSortOrder() != null) entity.setSortOrder(req.getSortOrder());

        // Ghi chú: Cập nhật danh mục cha (parent có thể là null).
        entity.setParent(parent);
    }

    // Ghi chú: Tạo Entity mới từ Request DTO.
    public static ProductCategory createEntity(CategoryRequest req, ProductCategory parent) {
        if (req == null) return null;

        return ProductCategory.builder()
                .categoryName(req.getCategoryName() != null ? req.getCategoryName() : "")
                .active(req.getIsActive() != null ? req.getIsActive() : Boolean.TRUE)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .parent(parent) // Ghi chú: Set danh mục cha
                .build();
    }
}