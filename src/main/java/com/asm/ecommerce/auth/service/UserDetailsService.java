package com.asm.ecommerce.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface lõi dùng để tải dữ liệu của một user.
 */
@FunctionalInterface
public interface UserDetailsService {

    /**
     * Tải thông tin user (thường là bằng username hoặc email).
     *
     * @param email Tên đăng nhập (trong trường hợp của bạn, là 'email')
     * @return một đối tượng UserDetails (mà lớp User của bạn sẽ triển khai)
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}