package com.asm.ecommerce.product.domain;

import com.asm.ecommerce.product.domain.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class Image {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    // Quan hệ với sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    private Product product;

    // Đường dẫn ảnh (Core field)
    @NotBlank
    @Size(max = 500)
    @Column(name = "secure_url", nullable = false, length = 500)
    private String secureUrl;

    // ✅ BỔ SUNG: Public ID (Cần để quản lý/xóa ảnh trên Cloudinary)
    @NotBlank
    @Size(max = 255)
    @Column(name = "public_id", nullable = false, length = 255)
    private String publicId;

    // ✅ BỔ SUNG: Alt Text (Cần thiết cho SEO và Trợ năng)
    @Column(columnDefinition = "text")
    private String altText;

    // Đánh dấu ảnh chính (Theo yêu cầu)
    @NotNull
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;


}