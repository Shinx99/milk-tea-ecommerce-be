package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ImageResponse;
import com.asm.ecommerce.product.dto.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductMapper {
    private ProductMapper() {}

    // Domain -> Response (ID từ Entity)
    public static ProductResponse toResponse (Product product) {
        if(product == null) return null;
        UUID categoryId = product.getCategory() != null ? product.getCategory().getId() : null;

        // Trong ProductMapper.java
        String imageUrl = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            imageUrl = product.getImages().stream()
                    // 1. TÌM ẢNH CHÍNH (isPrimary = TRUE)
                    .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                    .findFirst()
                    .map(Image::getSecureUrl)
                    // 2. NẾU KHÔNG CÓ ẢNH CHÍNH, LẤY ẢNH ĐẦU TIÊN CỦA DANH SÁCH
                    .orElse(product.getImages().get(0).getSecureUrl());
        }
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(categoryId)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(imageUrl)
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
    public static Product CreateEntity(ProductRequest req) {
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
    public static void UpdateEntity (Product entity, ProductRequest req) {
        if(entity == null || req == null) return;

        if(req.getName() != null) entity.setName(req.getName());
        if(req.getDescription() != null) entity.setDescription(req.getDescription());
        if(req.getQuantity() != null) entity.setQuantity(req.getQuantity());
        if(req.getPrice() != null) entity.setPrice(req.getPrice());
        if(req.getIsActive() != null) entity.setIsActive(req.getIsActive());
    }
}