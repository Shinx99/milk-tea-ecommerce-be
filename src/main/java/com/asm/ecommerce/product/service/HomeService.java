package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface HomeService {

    // Best Sellers toàn shop
    ApiResponse<PageResponse<ProductResponse>> getBestSellers(Pageable pageable);

    //Sản phẩm mới nhất toàn shop
    ApiResponse<PageResponse<ProductResponse>> getNewestProducts(Pageable pageable);
}
