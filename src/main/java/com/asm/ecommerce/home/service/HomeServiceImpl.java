package com.asm.ecommerce.home.service;

import com.asm.ecommerce.home.dto.response.HomeProductResponse;
import com.asm.ecommerce.home.mapper.HomeMapper;
import com.asm.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final ProductRepository productRepository;
    private final HomeMapper mapper;

    /** ✅ Best Sellers toàn shop */
    @Override
    public List<HomeProductResponse> getBestSellers(int limit) {
        return productRepository.findBestSellersOverall(limit)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /** ✅ Sản phẩm mới nhất toàn shop */
    @Override
    public List<HomeProductResponse> getNewestProducts(int limit) {
        return productRepository.findTopByNewest(limit)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
