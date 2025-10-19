package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product>findByCategoryId(UUID categoryId);
    List<Product> findByCategory_CategoryName(String categoryName);
    List<Product> findByNameContainingIgnoreCase(String name);}
