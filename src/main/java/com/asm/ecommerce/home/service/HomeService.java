package com.asm.ecommerce.home.service;

import com.asm.ecommerce.home.dto.response.HomeProductResponse;
import java.util.List;

public interface HomeService {
    List<HomeProductResponse> getBestSellers(int limit);
    List<HomeProductResponse> getNewestProducts(int limit);
}
