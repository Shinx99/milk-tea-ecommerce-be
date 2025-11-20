package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;

import java.util.List;

public interface CategoryService {

//    private final CategoryRepository repo;
//
//    // Ghi chú: Hàm tìm kiếm Category theo ID, ném ngoại lệ nếu không tìm thấy.
//    private ProductCategory findCategoryById(UUID id) {
//        return repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
//    }
//
//    // Ghi chú: Hàm tìm kiếm đối tượng cha theo parentId.
//    private ProductCategory getParentCategory(UUID parentId) {
//        if (parentId == null) {
//            return null;
//        }
//        return findCategoryById(parentId);
//    }
//
//    // Ghi chú: Hàm kiểm tra trùng lặp dựa trên ràng buộc UNIQUE INDEX (parent_id, category_name).
//    private Optional<ProductCategory> findDuplicateCategory(String name, UUID parentId) {
//        if (parentId == null) {
//            // Trường hợp cấp cao nhất
//            return repo.findByCategoryNameAndParentIsNull(name);
//        } else {
//            // Trường hợp cấp con
//            return repo.findByCategoryNameAndParentId(name, parentId);
//        }
//    }
//
//    // CREATE: Thêm mới Category
//    @Transactional
//    public CategoryResponse createCategory(CategoryRequest request) {
//        Category parent = getParentCategory(request.getParentId());
//
//        // Ghi chú: Kiểm tra trùng tên trong cùng cấp cha
//        Optional<ProductCategory> duplicate = findDuplicateCategory(request.getCategoryName(), request.getParentId());
//        if (duplicate.isPresent()) {
//            throw new RuntimeException("Category name '" + request.getCategoryName() + "' already exists under this parent.");
//        }
//
//        // Ghi chú: Tạo và lưu Entity
//        ProductCategory newCategory = CategoryMapper.createEntity(request, parent);
//        Category savedCategory = repo.save(newCategory);
//
//        return CategoryMapper.toResponse(savedCategory);
//    }
//
//    // READ: Lấy tất cả Category
//    @Transactional(readOnly = true)
//    public List<CategoryResponse> getAllCategories() {
//        // Ghi chú: Lấy tất cả, sắp xếp theo sortOrder và categoryName
//        List<ProductCategory> categories = repo.findAll(Sort.by(Sort.Direction.ASC, "sortOrder", "categoryName"));
//
//        return categories.stream()
//                .map(CategoryMapper::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // READ: Lấy Category theo ID
//    @Transactional(readOnly = true)
//    public CategoryResponse getCategoryById(UUID id) {
//        Category category = findCategoryById(id);
//        return CategoryMapper.toResponse(category);
//    }
//
//    // UPDATE: Cập nhật Category
//    @Transactional
//    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
//        ProductCategory existingCategory = findCategoryById(id);
//
//        // Ghi chú: Kiểm tra Parent Category mới
//        Category newParent = getParentCategory(request.getParentId());
//        UUID newParentId = request.getParentId();
//
//        // Ghi chú: Kiểm tra ràng buộc tự tham chiếu (CONSTRAINT ck_cat_parent_self)
//        if (newParent != null && newParent.getId().equals(id)) {
//            throw new RuntimeException("A category cannot be its own parent.");
//        }
//
//        // Ghi chú: Kiểm tra trùng tên trong cấp cha mới
//        Optional<ProductCategory> duplicate = findDuplicateCategory(request.getCategoryName(), newParentId);
//
//        duplicate.ifPresent(c -> {
//            // Ghi chú: Nếu tìm thấy trùng lặp và đó không phải là chính nó
//            if (!c.getId().equals(id)) {
//                throw new RuntimeException("Category name '" + request.getCategoryName() + "' already exists under this new parent.");
//            }
//        });
//
//        // Ghi chú: Cập nhật Entity và lưu
//        CategoryMapper.UpdateEntity(existingCategory, request, newParent);
//        ProductCategory updatedCategory = repo.save(existingCategory);
//
//        return CategoryMapper.toResponse(updatedCategory);
//    }
//
//    // DELETE: Xóa Category
//    @Transactional
//    public void deleteCategory(UUID id) {
//        Category categoryToDelete = findCategoryById(id);
//
//        // Ghi chú: Ngăn không cho xóa nếu có danh mục con hoặc sản phẩm liên quan.
//        if (!categoryToDelete.getChildren().isEmpty()) {
//            throw new RuntimeException("Cannot delete category: It has " + categoryToDelete.getChildren().size() + " child categories.");
//        }
//        if (!categoryToDelete.getProducts().isEmpty()) {
//            throw new RuntimeException("Cannot delete category: It has " + categoryToDelete.getProducts().size() + " associated products.");
//        }
//
//        repo.delete(categoryToDelete);
//    }

    ApiResponse<List<CategoryResponse>> loadCategoryForCombobox();
}