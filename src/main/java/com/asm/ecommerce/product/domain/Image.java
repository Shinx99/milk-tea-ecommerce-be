package com.asm.ecommerce.product.domain;

import com.asm.ecommerce.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "secure_url", nullable = false)
    private String secureUrl;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;
}
