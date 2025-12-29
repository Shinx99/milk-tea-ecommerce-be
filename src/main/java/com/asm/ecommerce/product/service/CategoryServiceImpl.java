package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.product.mapper.CategoryMapper;
import com.asm.ecommerce.product.repository.ProductCategoryRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final ProductCategoryRepository repo;

        //     TÌM KIẾM CATEGORYTHEO ID
    private ProductCategory findCategoryById(UUID id)   {
        return repo.findById(id).orElseThrow(() ->
                new RuntimeException("Category not found with id " + id));
    }

        //     TÌM VÀ KIỂM TRA PARENT CATEGORY
    private ProductCategory getParentCategory(UUID parentId){
        if(parentId == null){
            return null;
        }else {
            return findCategoryById(parentId);
        }
    }

        //      KIỂM TRA TRÙNG LẶP VỚI DỰA CẤP CHA
    private Optional<ProductCategory> findDuplicateCategory(String name, UUID parentId){
        if (parentId == null){
            return repo.findByCategoryNameAndParentIsNull(name);
        }else {
            return  repo.findByCategoryNameAndParentId(name,parentId);
        }
    }

    // CREATE
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        ProductCategory parent = getParentCategory(request.getParentId());
        Optional<ProductCategory> duplicate = findDuplicateCategory(request.getCategoryName(), request.getParentId());
        if (duplicate.isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        ProductCategory newCategory = CategoryMapper.createEntity(request, parent);

        return CategoryMapper.toResponse(repo.save(newCategory));
    }
    //  UPDATE
    @Override
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        ProductCategory existingCategory = findCategoryById(id);
        ProductCategory newParent = getParentCategory(request.getParentId());
        if(newParent != null && newParent.getId().equals(id)){
            throw new RuntimeException("A category cannot duplicate");
        }
        Optional<ProductCategory> duplicate = findDuplicateCategory(request.getCategoryName(), request.getParentId());
        duplicate.ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new RuntimeException("Error");
            }
        });
        CategoryMapper.UpdateEntity(existingCategory, request, newParent);
        return CategoryMapper.toResponse(repo.save(existingCategory));
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        ProductCategory category = findCategoryById(id);
        return CategoryMapper.toResponse(category);
    }


    @Override
    @Transactional
    public void deleteCategoryById(UUID id) {
        ProductCategory categoryToDelete = findCategoryById(id);
        if (!categoryToDelete.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete category: It has " + categoryToDelete.getChildren().size() + " child categories.");
        }
        if (!categoryToDelete.getProducts().isEmpty()) {
            throw new RuntimeException("Cannot delete category: It has " + categoryToDelete.getProducts().size() + " associated products.");
        }

        categoryToDelete.setActive(false);
        repo.save(categoryToDelete);
    }


    public ApiResponse<List<CategoryResponse>> loadCategoryForCombobox(){
        List<ProductCategory> categories = repo.findAllByParentIsNullAndActiveTrue();

        List<CategoryResponse> dto = CategoryMapper.toResponse(categories);

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Category for Combobox retrieved successfully!")
                .data(dto)
                .build();
    }

    public ApiResponse<List<CategoryResponse>> loadCategoryForDetail(){
        List<ProductCategory> categories = repo.findAllByActiveTrue();

        List<CategoryResponse> dto = CategoryMapper.toResponse(categories);

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Category for Product Detail retrieved successfully!")
                .data(dto)
                .build();
    }


    @Override
    public ApiResponse<PageResponse<CategoryResponse>> getAllCategories(String keyword, Pageable pageable) {

        Page<ProductCategory> categories = repo.findAll(keyword, pageable);

        PageResponse<CategoryResponse> pageResponse = PageResponse.<CategoryResponse>builder()
                .content(categories.getContent().stream()
                        .map(CategoryMapper::toResponse)
                        .toList())
                .pageNumber(categories.getNumber())
                .pageSize(categories.getSize())
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .last(categories.isLast())
                .build();

        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .success(true)
                .message("All categories retrieved for Admin Panel.")
                .data(pageResponse)
                .build();
    }

    //todo: ============= Cart ===================
    @Override
    public String getCategoryNameById(UUID categoryId) {
        ProductCategory category = repo.findById(categoryId)
                .orElse(null);
        return category != null ? category.getCategoryName() : null;
    }

    //For product admin---------------------------------------------------------------------------------------------------
    public ApiResponse<List<CategoryResponse>> loadCategoryForProductAdmin(){
        List<ProductCategory> categories = repo.findAllByParentIsNull();

        List<CategoryResponse> dto = CategoryMapper.toResponse(categories);

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Category for Combobox retrieved successfully!")
                .data(dto)
                .build();
    }
}
