package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.product.mapper.CategoryMapper;
import com.asm.ecommerce.product.repository.ProductCategoryRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final ProductCategoryRepository repo;

    public ApiResponse<List<CategoryResponse>> loadCategoryForCombobox(){
        List<ProductCategory> categories = repo.findAllByParentNull();

        List<CategoryResponse> dto = CategoryMapper.toResponse(categories);

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Category for Combobox retrieved successfully!")
                .data(dto)
                .build();
    }
}
