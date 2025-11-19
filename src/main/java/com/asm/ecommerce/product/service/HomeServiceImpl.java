package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.response.HomeProductResponse;
import com.asm.ecommerce.product.mapper.HomeMapper;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final ProductRepository productRepository;
    private final HomeMapper mapper;

    // Best Sellers toàn shop
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<HomeProductResponse>> getBestSellers(Pageable pageable) {
        Page<Product> products = productRepository.findBestSellersOverall(pageable);
        PageResponse<HomeProductResponse> pageResponse = PageResponse.<HomeProductResponse>builder()
                .content(products.getContent().stream()
                        .map(product -> mapper.toResponse(product))
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<HomeProductResponse>> builder()
                .success(true)
                .message("Home Product retrieved successfully")
                .data(pageResponse)
                .build();
    }

    //Sản phẩm mới nhất toàn shop
    @Override
    public ApiResponse<PageResponse<HomeProductResponse>> getNewestProducts(Pageable pageable) {
        Page<Product> products = productRepository.findTopByNewest(pageable);
        PageResponse<HomeProductResponse> pageResponse = PageResponse.<HomeProductResponse>builder()
                .content(products.getContent().stream()
                        .map(product -> mapper.toResponse(product))
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<HomeProductResponse>> builder()
                .success(true)
                .message("Newest Products retrieved successfully")
                .data(pageResponse)
                .build();
    }
}
