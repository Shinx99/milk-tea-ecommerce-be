package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.ProductCategory;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    List<ProductCategory> findAllByParentNull();

    List<ProductCategory> findAllByActiveTrue();
    // Kiểm tra không cho categoryName trùng tên UNIQUE
    Optional<ProductCategory> findByCategoryNameAndParentIsNull(String name);
    // Kiểm tra không cho categoryName vaf parent_id trùng nhau
    Optional<ProductCategory> findByCategoryNameAndParentId(String name, UUID parentId);

}
