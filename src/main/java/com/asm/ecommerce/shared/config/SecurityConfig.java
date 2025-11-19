package com.asm.ecommerce.shared.config;

import com.asm.ecommerce.shared.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
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

//    private final JwtAuthenticationFilter jwtAuthFilter; // <<< Inject Filter

    @Bean
    public UserDetailsService springSecurityUserDetailsService(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        // Adapter, dùng UserDetailsService custom của bạn
        return email -> userDetailsService.loadUserByUsername(email);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthFilter(
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            JwtUtil jwtUtil
    ) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                //.cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // 1.  Actuator endpoints - Public
                        .requestMatchers("/actuator/**").permitAll()

                        // 2.  Auth endpoints - Public
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password"
                        ).permitAll()

                        // 2.1. Internal API - Public (for inter-service communication)
                        .requestMatchers("/api/internal/**").permitAll()

                        .requestMatchers("/api/health", "/api/test/**").permitAll()

                        // 3.  Product browsing - Public
                        .requestMatchers(HttpMethod.GET, "/api/products/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/by-category-name").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/search").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/products/by-category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/detail/**").permitAll()

                        .requestMatchers("/api/products/**").authenticated()
                        .requestMatchers("/api/categories/**").permitAll()

                        .requestMatchers("/api/home/**").permitAll()


                        // 4.  Reviews - Public GET, authenticated POST/PUT/DELETE
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                        .requestMatchers("/api/reviews/**").authenticated()

                        // 5.  Admin endpoints - Require ADMIN role
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/statistics/**").hasRole("ADMIN")

                        // 6.  Protected customer endpoints - Require authentication
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/customers/**").authenticated()
                        .requestMatchers("/api/addresses/**").authenticated()
                        .requestMatchers("/api/payments/**").authenticated()
                        .requestMatchers("/api/vouchers/apply").authenticated()

                        // 7. Protected internal endpoint
                        .requestMatchers("/internal/**").permitAll() // chỉ bật ở profile dev/local


                        // 8.  Default - All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
