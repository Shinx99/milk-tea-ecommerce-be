package com.asm.ecommerce.customer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(
        name = "addresses",
        indexes = {
            @Index(name = "idx_addresses_customer_id", columnList = "customer_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_addresses_customer"))
    private Customer customer;

    @Column(name = "number", length = 50)
    private String number;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "ward", length = 100)
    private String ward;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    @Builder.Default
    private String city = "Ho Chi Minh City";

    @Column(name = "province", length = 100)
    @Builder.Default
    private String province = "HCM";

    @Column(name = "country", length = 100)
    @Builder.Default
    private String country = "VN";

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = true; // NOT NULL DEFAULT true

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean active = true; // NOT NULL DEFAULT true

}
