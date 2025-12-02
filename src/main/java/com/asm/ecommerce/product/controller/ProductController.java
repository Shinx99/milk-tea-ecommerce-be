package com.asm.ecommerce.product.controller;

import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.service.ProductService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") //Sao co cai 5173 o day vay? --> Vuong
public class ProductController {
    private final ProductService service;

    //Find all just for Admin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        ApiResponse<PageResponse<ProductResponse>> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    //Ham fetch product len trang Product + findByName + findByCategory
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> findAllByActiveTrue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
            ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        ApiResponse<PageResponse<ProductResponse>> response = service.findAllByActiveTrueAndFilter(keyword, category, pageable);
        return ResponseEntity.ok(response);
    }

    //Cho Product -> Category Combobox
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> findByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        ApiResponse<PageResponse<ProductResponse>> response = service.findByProductCategoryId(categoryId, pageable);
        return ResponseEntity.ok(response);
    }

    //Cho product details
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> findOne(@PathVariable UUID id) {
        ApiResponse<ProductResponse> response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    //Cho relate product
    @GetMapping("/relate/{id}")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> findRelateProduct(
            @PathVariable("id") UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction)
    {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        ApiResponse<PageResponse<ProductResponse>> response = service.findByCategoryIdAndIdNot(id, pageable);
        return ResponseEntity.ok(response);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    // ADMIN chua xu ly xong?
    //Ham fetch product len trang Product + findByName + findByCategory cho admin
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> findAllForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        ApiResponse<PageResponse<ProductResponse>> response = service.findAllByForAdmin(keyword, category, pageable);
        return ResponseEntity.ok(response);
    }

    // ===== CREATE =====
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        ProductResponse created = service.create(req);             // tạo
        return ResponseEntity
                .created(URI.create("/api/products/" + created.getId()))  // Location header
                .body(created);
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @RequestBody ProductRequest req) {
        ProductResponse updated = service.update(id, req); // update
        return ResponseEntity.ok(updated);
    }


    // ===== DELETE =====
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();                 // 204 No Content
    }
}

