package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    ApiResponse<List<CategoryResponse>> loadCategoryForCombobox();

    ApiResponse<List<CategoryResponse>> loadCategoryForDetail();

    //List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(UUID id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory (UUID id, CategoryRequest request);
    void deleteCategoryById(UUID id);


}