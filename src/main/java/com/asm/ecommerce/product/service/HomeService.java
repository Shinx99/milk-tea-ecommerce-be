package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.response.HomeProductResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface HomeService {

    // Best Sellers toàn shop
    ApiResponse<PageResponse<HomeProductResponse>> getBestSellers(Pageable pageable);

    //Sản phẩm mới nhất toàn shop
    ApiResponse<PageResponse<HomeProductResponse>> getNewestProducts(Pageable pageable);
}
