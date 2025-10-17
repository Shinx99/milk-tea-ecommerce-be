package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductMapper {
    private ProductMapper() {}

    // Domain -> Response (Sửa để lấy ID từ Entity)
    public static ProductResponse toResponse (Product product) {
        if(product == null) return null;
        // Lấy categoryId từ mối quan hệ Category Entity
        UUID categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(categoryId)
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    // List<Domain>
    public static List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        List<ProductResponse> list = new ArrayList<>();
        for (Product p : products) {
            if (p != null) {
                list.add(toResponse(p));
            }
        }
        return list;
    }

    // CREATE (Request -> Domain)
    public static Product toEntiy(ProductRequest req) {
        if(req == null) return null;
        return Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .isActive(req.getIsActive())
                .build();
    }

    // UPDATE (Apply Request -> Domain)
    public static void applyUpdate (Product entity, ProductRequest req) {
        if(entity == null || req == null) return;
        if(req.getName() != null) entity.setName(req.getName());
        if(req.getDescription() != null) entity.setDescription(req.getDescription());
        if(req.getQuantity() != null) entity.setQuantity(req.getQuantity());
        if(req.getPrice() != null) entity.setPrice(req.getPrice());
        if(req.getIsActive() != null) entity.setIsActive(req.getIsActive());
    }
}