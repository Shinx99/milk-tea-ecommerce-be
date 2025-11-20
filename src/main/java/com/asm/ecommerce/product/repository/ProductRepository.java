package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    //Find by Category
    @Query("""
    SELECT p
    FROM Product p
    WHERE p.category.id = :categoryId
      AND p.category.active = true
      AND p.category.parent IS NULL
""")
    Page<Product> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);


    Page<Product> findByCategory_CategoryNameAndActiveTrue(String categoryName, Pageable pageable);

    //Product
    //FindByProductName
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);


    @Query(value = """
    SELECT p.*
    FROM products p
    LEFT JOIN order_items oi ON oi.product_id = p.id
    WHERE p.is_active = true
    GROUP BY p.id
    ORDER BY COALESCE(SUM(oi.quantity), 0) DESC, p.created_at DESC
    """, nativeQuery = true)
    Page<Product> findBestSellersOverall(Pageable pageable);

    @Query(value = """
    SELECT p.*
    FROM products p
    WHERE p.is_active = true
    ORDER BY p.created_at DESC
    """, nativeQuery = true)
    Page<Product> findTopByNewest(Pageable pageable);


    Page<Product> findAllByActiveTrue(Pageable pageable);
}
