package com.asm.ecommerce.product.controller;

import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") //Sao co cai 5173 o day vay? --> Vuong
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ProductResponse> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 1000)
                                         Pageable pageable) {
        return service.findAll(); }

    @GetMapping("/by-category-name")
    public List<ProductResponse> findByCategoryName(@RequestParam("name") String categoryName) {
        return service.findByProductCategoryName(categoryName);
    }

    @GetMapping("/by-category/{categoryId}")
    public List<ProductResponse> findByCategory(@PathVariable UUID categoryId) {
        return service.findByProductCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    public ProductResponse findOne(@PathVariable UUID id) {

        return service.findById(id);
    }


    // POST /api/products → tạo mới
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        ProductResponse created = service.create(req);             // tạo
        return ResponseEntity
                .created(URI.create("/api/products/" + created.getId()))  // Location header
                .body(created);
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest req) {
        return service.update(id, req);
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();                 // 204 No Content
    }

    // ===== SEARRCH =====
    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam("name") String name) {
        return service.searchProductsByName(name);
    }
}

