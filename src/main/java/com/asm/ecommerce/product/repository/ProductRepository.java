package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product>findByCategoryId(UUID categoryId);
    List<Product> findByCategory_CategoryName(String categoryName);
    List<Product> findByNameContainingIgnoreCase(String name);
    // --- Hải Mới thêm---
    /** ✅ Best-sellers theo danh mục cha (VD: "Milk Tea", "Fruit Tea") */
    @Query(value = """
        SELECT p.*
        FROM products p
        JOIN categories c ON p.category_id = c.id
        JOIN categories parent ON c.parent_id = parent.id
        WHERE parent.category_name = :parentCategory
          AND p.is_active = true
        ORDER BY p.created_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Product> findBestSellersByParentCategory(
            @Param("parentCategory") String parentCategory,
            @Param("limit") int limit
    );

    /** ✅ Sản phẩm mới nhất (new arrivals) */
    @Query(value = """
        SELECT p.*
        FROM products p
        WHERE p.is_active = true
        ORDER BY p.created_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Product> findTopByNewest(@Param("limit") int limit);
}
