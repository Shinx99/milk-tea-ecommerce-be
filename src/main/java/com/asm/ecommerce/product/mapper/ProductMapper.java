package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductMapper {
    private ProductMapper() {}

    public static ProductResponse toResponse (Product product) {
        if(product == null) return null;
        UUID categoryId = product.getCategory() != null ? product.getCategory().getId() : null;

        List<String> images = List.of();
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            images = product.getImages().stream()
                    .map(Image::getSecureUrl)
                    .collect(Collectors.toList());
        }
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(categoryId)
                .name(product.getName())
                .quantity(product.getQuantity())
                .isActive(product.getIsActive())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(images) // Dùng tên imageUrl
                .build();
    }

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

    public static void UpdateEntity (Product entity, ProductRequest req) {
        if(entity == null || req == null) return;

        if(req.getName() != null) entity.setName(req.getName());
        if(req.getDescription() != null) entity.setDescription(req.getDescription());
        if(req.getQuantity() != null) entity.setQuantity(req.getQuantity());
        if(req.getPrice() != null) entity.setPrice(req.getPrice());
        if(req.getIsActive() != null) entity.setIsActive(req.getIsActive());
    }
}