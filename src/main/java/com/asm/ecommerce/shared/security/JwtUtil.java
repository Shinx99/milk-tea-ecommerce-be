package com.asm.ecommerce.shared.security;

import com.asm.ecommerce.auth.dto.response.AuthResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private long jwtExpiry;             // milliseconds

    public void init(){
        // Optional: validate secret, check expiry, logging...
    }

   public String generateToken(String userId, String email, String role){
        return Jwts.builder()
                .setSubject(userId)
                .claim("email",email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiry))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
   }

}
