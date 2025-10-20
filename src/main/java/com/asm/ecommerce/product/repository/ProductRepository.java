package com.asm.ecommerce.product.repository;

import com.asm.ecommerce.product.domain.Product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCategory_CategoryName(String categoryName);
    List<Product> findByNameContainingIgnoreCase(String name);

    // ✅ Best-sellers toàn shop (không phân loại theo danh mục)
    // sắp theo tổng quantity bán được, nếu chưa bán gì thì fallback theo created_at

    @Override
    @EntityGraph(attributePaths = {"category", "images"})
    List<Product> findAll();

    @Override
    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findById(UUID uuid);
    // --- Hải Mới thêm---
    /** ✅ Best-sellers theo danh mục cha (VD: "Milk Tea", "Fruit Tea") */
    @Query(value = """
        SELECT p.*
        FROM products p
        LEFT JOIN order_items oi ON oi.product_id = p.id
        WHERE p.is_active = true
        GROUP BY p.id
        ORDER BY COALESCE(SUM(oi.quantity), 0) DESC, p.created_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Product> findBestSellersOverall(@Param("limit") int limit);

    // ✅ Sản phẩm mới nhất toàn shop
    @Query(value = """
        SELECT p.*
        FROM products p
        WHERE p.is_active = true
        ORDER BY p.created_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Product> findTopByNewest(@Param("limit") int limit);
}
