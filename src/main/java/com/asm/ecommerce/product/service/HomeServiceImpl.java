package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.mapper.ProductMapper;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final ProductRepository productRepository;

    // Best Sellers toàn shop
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<ProductResponse>> getBestSellers(Pageable pageable) {
        // 1. Lấy danh sách ID sản phẩm bán chạy (đã phân trang)
        Page<UUID> idPage = productRepository.findBestSellerIds(pageable);

        // 2. Lấy nội dung chi tiết sản phẩm từ danh sách ID
        List<ProductResponse> productResponses = fetchProductDetails(idPage.getContent());

        // 3. Đóng gói response
        PageResponse<ProductResponse> pageResponse = buildPageResponse(idPage, productResponses);

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Home Best Sellers retrieved successfully")
                .data(pageResponse)
                .build();
    }

    // Sản phẩm mới nhất toàn shop
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<ProductResponse>> getNewestProducts(Pageable pageable) {
        // 1. Lấy danh sách ID sản phẩm mới nhất (đã phân trang)
        Page<UUID> idPage = productRepository.findNewestIds(pageable);

        // 2. Lấy nội dung chi tiết sản phẩm từ danh sách ID
        List<ProductResponse> productResponses = fetchProductDetails(idPage.getContent());

        // 3. Đóng gói response
        PageResponse<ProductResponse> pageResponse = buildPageResponse(idPage, productResponses);

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Newest Products retrieved successfully")
                .data(pageResponse)
                .build();
    }

    // --- Helper Method: Fetch chi tiết và giữ đúng thứ tự ---
    private List<ProductResponse> fetchProductDetails(List<UUID> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch toàn bộ sản phẩm (kèm Category & Images) bằng 1 query IN
        List<Product> products = productRepository.findAllByIds(ids);

        // Tạo Map để tra cứu nhanh: ID -> Product
        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Map lại theo đúng thứ tự của list 'ids' ban đầu
        return ids.stream()
                .map(productMap::get)         // Lấy Product từ Map
                .filter(Objects::nonNull)     // Lọc bỏ null (đề phòng database mất đồng bộ)
                .map(ProductMapper::toResponse) // Convert sang DTO
                .toList();
    }

    // --- Helper Method: Build PageResponse ---
    private PageResponse<ProductResponse> buildPageResponse(Page<UUID> pageInfo, List<ProductResponse> content) {
        return PageResponse.<ProductResponse>builder()
                .content(content)
                .pageNumber(pageInfo.getNumber())
                .pageSize(pageInfo.getSize())
                .totalPages(pageInfo.getTotalPages())
                .totalElements(pageInfo.getTotalElements())
                .last(pageInfo.isLast())
                .build();
    }
}
