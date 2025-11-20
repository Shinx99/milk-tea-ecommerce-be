package com.asm.ecommerce.product.controller;

import com.asm.ecommerce.product.dto.request.CategoryRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.product.service.CategoryService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
//
//    // Ghi chú: POST /api/categories (Create)
//    @PostMapping
//    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
//        return new ResponseEntity<>(service.createCategory(request), HttpStatus.CREATED); // 201 Created
//    }
//
//    // Ghi chú: GET /api/categories (Read All)
//    @GetMapping
//    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
//        return ResponseEntity.ok(service.getAllCategories());
//    }
//
//    // Ghi chú: GET /api/categories/{id} (Read One)
//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
//        return ResponseEntity.ok(service.getCategoryById(id));
//    }
//
//    // Ghi chú: PUT /api/categories/{id} (Update)
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id,
//                                                           @RequestBody @Valid CategoryRequest request) {
//        return ResponseEntity.ok(service.updateCategory(id, request));
//    }
//
//    // Ghi chú: DELETE /api/categories/{id} (Delete)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
//        service.deleteCategory(id);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> loadCategoryForCombobox(){
        ApiResponse<List<CategoryResponse>> response = service.loadCategoryForCombobox();

        return ResponseEntity.ok(response);
    }
}