package com.asm.ecommerce.shared.security; // Hoặc một package 'security' hợp lý

import com.asm.ecommerce.auth.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String email;
    @JsonIgnore
    private final String password;
    private final boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.isActive = user.getIsActive();

        if (user.getRole() != null && user.getRole().getRole() != null && !user.getRole().getRole().isEmpty()) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole().toUpperCase());
            this.authorities = List.of(authority);
        } else {
            this.authorities = List.of();
        }
    }

    // Getter cho các trường tùy chỉnh của bạn
    public UUID getId() {
        return id;
    }

    // =========================================================
    // CÁC PHƯƠNG THỨC BẮT BUỘC CỦA UserDetails
    // =========================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
