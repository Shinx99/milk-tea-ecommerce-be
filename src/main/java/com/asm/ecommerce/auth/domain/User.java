/*
package com.asm.ecommerce.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // ✅ Field chính để insert/update
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    // ✅ Field để fetch relationship (read-only)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "is_active")
    @Builder.Default  // ✅ Thêm annotation này
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}

*/




package com.asm.ecommerce.auth.domain;

import jakarta.persistence.*;
        import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
        import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// <<< 1. THÊM CÁC IMPORT CẦN THIẾT
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails { // <<< 2. THÊM "implements UserDetails"

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // ✅ Field chính để insert/update
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    // ✅ Field để fetch relationship (read-only)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;


    // =========================================================
    // <<< 3. THÊM CÁC PHƯƠNG THỨC CỦA UserDetails
    // =========================================================

    @Override
    @Transient // Báo cho JPA bỏ qua, không map vào DB
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // (Giả sử Role entity của bạn có phương thức getRole() trả về "ROLE_ADMIN")
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    }

    @Override
    public String getPassword() {
        // Trả về trường password đã hash
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        // Trả về trường bạn dùng để đăng nhập (là email)
        return this.email;
    }

    @Override
    @Transient // Báo cho JPA bỏ qua
    public boolean isAccountNonExpired() {
        return true; // Mặc định là true
    }

    @Override
    @Transient // Báo cho JPA bỏ qua
    public boolean isAccountNonLocked() {
        return true; // Mặc định là true
    }

    @Override
    @Transient // Báo cho JPA bỏ qua
    public boolean isCredentialsNonExpired() {
        return true; // Mặc định là true
    }

    @Override
    public boolean isEnabled() {
        // Trả về trường isActive của bạn
        return this.isActive;
    }
}
