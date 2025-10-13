package com.asm.ecommerce.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

 /*
     1. Public endpoints (permitAll)
     - /actuator*//**
     - /api/auth/**
     - /api/products/**

     2. Conditional endpoints (specific methods)
     - GET /api/reviews/** → permitAll
     - POST/PUT/DELETE /api/reviews/** → authenticated

     3. Role-based endpoints
     - /api/admin/** → ADMIN role

     4. Protected endpoints (authenticated)
     - /api/cart/**
     - /api/orders/**

     5. Default rule (CUỐI CÙNG)
     - anyRequest() → authenticated
*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // 1. ⭐ Actuator endpoints - Public
                        .requestMatchers("/actuator/**").permitAll()

                        // 2. ⭐ Auth endpoints - Public
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health", "/api/test/**").permitAll()

                        // 3. ⭐ Product browsing - Public
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()

                        // 4. ⭐ Reviews - Public GET, authenticated POST/PUT/DELETE
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                        .requestMatchers("/api/reviews/**").authenticated()

                        // 5. ⭐ Admin endpoints - Require ADMIN role
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/statistics/**").hasRole("ADMIN")

                        // 6. ⭐ Protected customer endpoints - Require authentication
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/customers/**").authenticated()
                        .requestMatchers("/api/payments/**").authenticated()
                        .requestMatchers("/api/vouchers/apply").authenticated()

                        // 7. ⭐ Default - All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
