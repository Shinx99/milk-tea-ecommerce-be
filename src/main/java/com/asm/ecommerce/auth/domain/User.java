package com.asm.ecommerce.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;  // ⭐ Import
import org.hibernate.annotations.UpdateTimestamp;    // ⭐ Import

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private UUID id;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank
    private String passwordHash;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)  // ⭐ updatable = false (không update khi save)
    @CreationTimestamp  // ⭐ Hibernate tự động set khi create
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp    // ⭐ Hibernate tự động update mỗi lần save
    private Instant updatedAt;

    @Column(name = "role_id")
    private UUID roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
}
