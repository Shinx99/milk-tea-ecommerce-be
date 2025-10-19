package com.asm.ecommerce.category.mapper;

import com.asm.ecommerce.category.domain.Category;
import com.asm.ecommerce.category.dto.request.CategoryRequest;
import com.asm.ecommerce.category.dto.response.CategoryResponse;

import java.util.UUID;

public class CategoryMapper {
    private CategoryMapper() {}

    // Ghi chú: Chuyển đổi Entity sang Response DTO.
    public static CategoryResponse toResponse (Category category){
        if(category == null) return null;
        UUID parentId = category.getParent() != null
                ? category.getParent().getId() : null;

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .parentId(parentId)
                .sortOrder(category.getSortOrder())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    // Ghi chú: Cập nhật Entity hiện có bằng dữ liệu từ Request DTO.
    public static void UpdateEntity (Category entity, CategoryRequest req, Category parent){
        if(entity == null || req == null) return;

        // Ghi chú: Chỉ cập nhật trường nếu dữ liệu mới được cung cấp (không null).
        if(req.getCategoryName() != null) entity.setCategoryName(req.getCategoryName());
        if(req.getIsActive() !=null) entity.setIsActive(req.getIsActive());
        if(req.getSortOrder() != null) entity.setSortOrder(req.getSortOrder());

        // Ghi chú: Cập nhật danh mục cha (parent có thể là null).
        entity.setParent(parent);
    }

    // Ghi chú: Tạo Entity mới từ Request DTO.
    public static Category createEntity(CategoryRequest req, Category parent) {
        if (req == null) return null;

        return Category.builder()
                .categoryName(req.getCategoryName() != null ? req.getCategoryName() : "")
                .isActive(req.getIsActive() != null ? req.getIsActive() : Boolean.TRUE)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .parent(parent) // Ghi chú: Set danh mục cha
                .build();
    }
}