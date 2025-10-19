package com.asm.ecommerce.home.service;

import com.asm.ecommerce.home.dto.response.HomeProductResponse;
import com.asm.ecommerce.home.mapper.HomeMapper;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.repository.ProductRepository;
//import com.asm.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final ProductRepository productRepository;
    //private final CategoryRepository categoryRepository;
    private final HomeMapper mapper;

    @Override
    public List<HomeProductResponse> getBestSellers(String parentCategory, int limit) {
        return productRepository.findBestSellersByParentCategory(parentCategory, limit)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HomeProductResponse> getNewestProducts(int limit) {
        return productRepository.findTopByNewest(limit)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
