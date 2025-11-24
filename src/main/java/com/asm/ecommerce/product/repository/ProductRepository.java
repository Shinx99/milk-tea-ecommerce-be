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

    @EntityGraph(attributePaths = {"category", "images"})
    Page<Product> findByCategory_CategoryNameAndActiveTrue(String categoryName, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "images"})
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);


    // 1. Hàm tìm ID sản phẩm bán chạy (SQL thuần)
    // Trả về List<UUID> thay vì Page<Product> để nhẹ gánh
    @Query(value = """
        SELECT p.id
        FROM products p
        LEFT JOIN order_items oi ON oi.product_id = p.id
        WHERE p.is_active = true
        GROUP BY p.id
        ORDER BY COALESCE(SUM(oi.quantity), 0) DESC, p.created_at DESC
        """, nativeQuery = true)
    Page<UUID> findBestSellerIds(Pageable pageable);


    // 2. Hàm tìm ID sản phẩm mới nhất (SQL thuần)
    @Query(value = """
        SELECT p.id 
        FROM products p 
        WHERE p.is_active = true 
        ORDER BY p.created_at DESC
        """, nativeQuery = true)
    Page<UUID> findNewestIds(Pageable pageable);


    // 3. Hàm quan trọng: Lấy chi tiết Product theo danh sách ID
    // Dùng EntityGraph để fetch luôn Category và Images
    @EntityGraph(attributePaths = {"category", "images"})
    @Query("SELECT DISTINCT p FROM Product p WHERE p.id IN :ids AND p.active = true")
    List<Product> findAllByIds(@Param("ids") List<UUID> ids);

    //Ham nay cho Product Detail
    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findByIdAndActiveTrue(UUID id);

    //Product
    //FindByProductName
    // Nó báo cho Hibernate: "Khi chạy hàm dưới, hãy JOIN luôn bảng category và images vào ngay lập tức"
    @EntityGraph(attributePaths = {"category", "images"})
    Page<Product> findAllByActiveTrue(Pageable pageable);
}
