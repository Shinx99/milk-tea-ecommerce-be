package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductService {

    ApiResponse<PageResponse<ProductResponse>> searchProductsByName(String name, Pageable pageable);

    ApiResponse<PageResponse<ProductResponse>> findByProductCategoryName(String categoryName, Pageable pageable);

    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findByProductCategoryId(UUID categoryId, Pageable pageable);

    ApiResponse<ProductResponse> findById(UUID id);

    // Display
    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findAllByActiveTrue(Pageable pageable);

    ProductResponse create(ProductRequest request);
    ProductResponse update(UUID id, ProductRequest request);
    void delete(UUID id);


}