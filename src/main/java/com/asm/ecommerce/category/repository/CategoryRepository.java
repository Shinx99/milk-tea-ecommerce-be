package com.asm.ecommerce.category.repository;

import com.asm.ecommerce.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Ghi chú: Tìm kiếm danh mục con có tên và cha cụ thể. Phù hợp với UNIQUE INDEX (parent_id, category_name).
    Optional<Category> findByCategoryNameAndParentId(String categoryName, UUID parentId);

    // Ghi chú: Tìm kiếm danh mục cấp cao nhất (parent_id IS NULL) theo tên.
    Optional<Category> findByCategoryNameAndParentIsNull(String categoryName);
}
