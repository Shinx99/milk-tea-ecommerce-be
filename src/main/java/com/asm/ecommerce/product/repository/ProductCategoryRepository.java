package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    List<ProductCategory> findAllByParentNull();

    List<ProductCategory> findAllByActiveTrue();
}
