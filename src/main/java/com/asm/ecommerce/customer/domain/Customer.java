package com.asm.ecommerce.customer.domain;

import com.asm.ecommerce.auth.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customer_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_customer_user_id", columnNames = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    // --- BẮT ĐẦU PHẦN THIẾT KẾ ĐÁNH ĐỔI ---

    // 1. TRƯỜNG DÙNG ĐỂ GHI (INSERT/UPDATE)
    // - Dùng để nhận UUID từ DTO/Request một cách tiện lợi.
    // - Đây là "nguồn chân lý" khi lưu vào DB.
    @Column(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID userId;

    // 2. TRƯỜNG DÙNG ĐỂ ĐỌC (READ-ONLY RELATIONSHIP)
    // - Dùng để lấy thông tin đầy đủ của User khi cần.
    // - `insertable = false, updatable = false` là CỰC KỲ QUAN TRỌNG.
    //   Nó báo cho JPA biết rằng trường này không tham gia vào việc ghi dữ liệu,
    //   tránh lỗi "duplicate mapping".
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // --- KẾT THÚC PHẦN THIẾT KẾ ĐÁNH ĐỔI ---

    @NotBlank
    @Column(name = "phone", nullable = false, length = 40)
    private String phone;

    @NotBlank
    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
