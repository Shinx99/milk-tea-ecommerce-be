package com.asm.ecommerce.category.domain;

import com.asm.ecommerce.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity // Ghi chú: Đánh dấu class này là một JPA Entity, ánh xạ tới một bảng DB.
@Table(name = "categories",
        // Ghi chú: Định nghĩa Unique Constraint theo database (parent_id, category_name)
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"parent_id", "category_name"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @Column(name = "category_name", nullable = false, length = 255)
    private String categoryName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    // Ghi chú: ManyToOne (Child -> Parent). Trường này lưu trữ khóa ngoại parent_id.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Ghi chú: OneToMany (Parent -> Children). Mối quan hệ được ánh xạ bởi trường 'parent' ở phía con.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    // Ghi chú: Quan hệ OneToMany với Product.
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // Ghi chú: Tự động điền thời gian tạo.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Ghi chú: Tự động cập nhật thời gian sửa đổi.
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}