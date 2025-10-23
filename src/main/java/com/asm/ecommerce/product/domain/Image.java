package com.asm.ecommerce.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    private Product product;

    @NotBlank
    @Size(max = 500)
    @Column(name = "secure_url", nullable = false, length = 500)
    private String secureUrl;

    // Trường này cần giá trị, ngay cả khi là giá trị mặc định trong Service
    @NotBlank
    @Size(max = 255)
    @Column(name = "public_id", nullable = false, length = 255)
    private String publicId;

    @Column(columnDefinition = "text")
    private String altText;

    @NotNull
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
}