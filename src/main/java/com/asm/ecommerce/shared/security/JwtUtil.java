package com.asm.ecommerce.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct; // Hoặc dùng javax.annotation nếu project không tự xử lý jakarta

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 1. DI CHUYỂN: Khai báo secret trước để giá trị được inject
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private long jwtExpiry;

    private Key key;

    // 2. POSTCONSTRUCT: Khởi tạo Key (chắc chắn dùng Key cho API mới)
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Tạo token sử dụng API mới.
     */
    public String generateToken(String userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiry))
                // Dùng signWith(Key, Algorithm)
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Các phương thức Filter ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (userDetails != null && username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    /**
     * Trích xuất tất cả claims dùng API mới (parserBuilder()...build()).
*/
    private Claims extractAllClaims(String token) {
        try {
            // Dùng Key đã được tạo
            return Jwts.parser()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            logger.error("Lỗi khi phân tích token JWT: {}", e.getMessage());
            return null;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}