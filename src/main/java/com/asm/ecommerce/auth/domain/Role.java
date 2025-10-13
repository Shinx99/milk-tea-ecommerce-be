package com.asm.ecommerce.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;  // ⭐ Đổi sang Instant
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private UUID id;

    private String role;

    private String description;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;  // ⭐ Instant

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;  // ⭐ Instant

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;
}
