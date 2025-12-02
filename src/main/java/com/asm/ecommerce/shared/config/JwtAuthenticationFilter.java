package com.asm.ecommerce.shared.config; // Or .security

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.shared.security.JwtUtil;
import com.asm.ecommerce.shared.security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
// <<< 1. Import SLF4J Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final Logger filterLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Bỏ qua các endpoint public
        if (path.startsWith("/api/chat")
                || path.startsWith("/api/auth")
                || path.startsWith("/api/health")
                || path.startsWith("/api/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Khi request di toi -> Filter Hoi xem ve moi cua khach(request) co phai la Authorization khong!
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtUtil.extractUsername(jwt); // Calls method from JwtUtil

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtUtil.isTokenValid(jwt, userDetails)) { // Calls method from JwtUtil

                    UserPrincipal userPrincipal = (UserPrincipal) userDetails;
                    UUID userId = userPrincipal.getId();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //bo sung them thong tin vi du nhu IP cho authentication --> Logs, chong hacker
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // <<< 3. FIX THE LOGGER CALL HERE
            filterLogger.error("Cannot set user authentication: {}", e.getMessage()); // Use SLF4J format

            // Optionally send 401 response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid Token");
            // Do NOT call filterChain.doFilter(request, response); here
        }
    }
}
