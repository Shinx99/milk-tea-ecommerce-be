package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.response.HomeProductResponse;
import org.springframework.stereotype.Component;

@Component
public class HomeMapper {
    public HomeProductResponse toResponse(Product product) {
        String imageUrl = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            imageUrl = product.getImages().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                    .findFirst()
                    .map(img -> img.getSecureUrl())
                    .orElse(product.getImages().get(0).getSecureUrl());
        }
        return new HomeProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                imageUrl
        );
    }
}
