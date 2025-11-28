package com.asm.ecommerce.product.controller;

import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.product.service.CategoryService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    // -------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        CategoryResponse category = service.getCategoryById(id); // Ghi chú: Gọi Service tìm theo ID.
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = service.createCategory(request); // Ghi chú: Gọi Service để lưu vào DB.

        // Ghi chú: Trả về kết quả và Status 201 CREATED (Tạo mới thành công).
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @RequestBody @Valid CategoryRequest request) {

        CategoryResponse updatedCategory = service.updateCategory(id, request);

        // Ghi chú: Trả về kết quả và Status 200 OK.
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        // Ghi chú: Gọi Service để xóa Category (Service sẽ kiểm tra ràng buộc).
        service.deleteCategoryById(id);

        // Ghi chú: Trả về Status 204 NO CONTENT (Xóa thành công, không trả về dữ liệu).
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> loadCategoryForCombobox(){
        ApiResponse<List<CategoryResponse>> response = service.loadCategoryForCombobox();

        return ResponseEntity.ok(response);
    }

    // url: .../api/categories/productdetail
    @GetMapping("/productdetail")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> loadCategoryForProductDetail(){
        ApiResponse<List<CategoryResponse>> response = service.loadCategoryForDetail();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getAllCategoriesForAdmin(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction
    ){
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        ApiResponse<PageResponse<CategoryResponse>> response = service.getAllCategories(keyword, pageable);
        return ResponseEntity.ok(response);
    }


}