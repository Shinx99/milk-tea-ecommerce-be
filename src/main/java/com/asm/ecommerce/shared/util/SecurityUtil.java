package com.asm.ecommerce.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtil {

    /**
     * Get current authenticated user ID from security context
     */
    //Vuong
    //Method nay dang hoi co nhieu Bugs
    //Lop Util nay co the duoc
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // Assuming principal is UUID (customize based on your UserDetails implementation)
        if (principal instanceof UUID) {
            return (UUID) principal;
        }

        // If principal is String (username or ID as string)
        if (principal instanceof String) {
            try {
                return UUID.fromString((String) principal);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Get current authenticated username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getName();
    }

    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Check if current user has specific role
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    /**
     * Check if current user has any of the specified roles
     */
    public static boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    private SecurityUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
