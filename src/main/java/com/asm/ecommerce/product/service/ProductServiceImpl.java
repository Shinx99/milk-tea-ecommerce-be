package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.mapper.ProductMapper;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        List<Product> products = repo.findAll();
        return ProductMapper.toResponseList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByProductCategoryName(String categoryName) {

        return ProductMapper.toResponseList(repo.findByCategory_CategoryName(categoryName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByProductCategoryId(UUID categoryId) {
        return ProductMapper.toResponseList(repo.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        return ProductMapper.toResponse(p);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product entity = ProductMapper.toEntiy(request);
        Product saved = repo.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest request) {
        Product entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        ProductMapper.applyUpdate(entity, request);
        Product saved = repo.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found ");
        }
        repo.deleteById(id);
    }

}


