// controller/HomeController.java
package com.asm.ecommerce.product.controller;

import com.asm.ecommerce.product.dto.response.HomeProductResponse;
import com.asm.ecommerce.product.service.HomeService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/best-sellers")
    public ResponseEntity<ApiResponse<PageResponse<HomeProductResponse>>> getBestSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<PageResponse<HomeProductResponse>> response = homeService.getBestSellers(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/newest")
    public ResponseEntity<ApiResponse<PageResponse<HomeProductResponse>>> getNewest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<PageResponse<HomeProductResponse>> response = homeService.getNewestProducts(pageable);

        return ResponseEntity.ok(response);
    }
}
