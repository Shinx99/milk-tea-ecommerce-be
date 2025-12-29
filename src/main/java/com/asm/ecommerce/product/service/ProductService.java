package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findAllByActiveTrueAndFilter(String keyword, String categoryName, Pageable pageable);

    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findByProductCategoryId(UUID categoryId, Pageable pageable);

    ApiResponse<ProductResponse> findById(UUID id);

    // Display
    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findAll(Pageable pageable);

    //Cho method relatedProduct
    @Transactional
    ApiResponse<PageResponse<ProductResponse>> findByCategoryIdAndIdNot(UUID excludeId, Pageable pageable);

    // for admin-------------------------------------------------------------------------------------------------------------------
    // Method: Hien thi cho ca trang Product
    //Display for Customer
    @Transactional(readOnly = true)
    ApiResponse<PageResponse<ProductResponse>> findAllByForAdmin(String keyword, String categoryName, Pageable pageable);

    ProductResponse create(ProductRequest request);

    ProductResponse update(UUID id, ProductRequest request);
    void delete(UUID id);

    // for cart-------------------------------------------------------------------------------------------------------------------
    //todo: === Cart =====
    List<ProductResponse> searchProductsByName(String name);
    ProductResponse getProductInfoForCart(UUID productId);



}