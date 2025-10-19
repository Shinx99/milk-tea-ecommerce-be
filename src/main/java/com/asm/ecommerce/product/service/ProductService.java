package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> findAll();
    List<ProductResponse> findByProductCategoryName(String categoryName);
    List<ProductResponse> findByProductCategoryId(UUID categoryId);
    ProductResponse findById(UUID id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(UUID id, ProductRequest request);
    void delete(UUID id);
    List<ProductResponse> searchProductsByName(String name);
}