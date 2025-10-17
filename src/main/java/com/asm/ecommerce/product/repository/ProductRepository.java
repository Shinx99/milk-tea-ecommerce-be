package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product>findByCategoryId(UUID categoryId);
    List<Product> findByCategory_CategoryName(String categoryName);

}
