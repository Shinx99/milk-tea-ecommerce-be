package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    ApiResponse<List<CategoryResponse>> loadCategoryForCombobox();

    ApiResponse<List<CategoryResponse>> loadCategoryForDetail();

    CategoryResponse getCategoryById(UUID id);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory (UUID id, CategoryRequest request);

    void deleteCategoryById(UUID id);

    ApiResponse<PageResponse<CategoryResponse>> getAllCategories(String keyword, Pageable pageable);

    //todo: ==== Cart =====
    String getCategoryNameById(UUID categoryId);

}