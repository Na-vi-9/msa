package com.sparta.msa.ai.infrastructure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtils {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public String extractUsername(String token) {
        Claims claims = getClaims(token);

        // username 필드가 있으면 가져오고, 없으면 subject 반환
        String username = claims.get("username", String.class);
        if (username == null || username.trim().isEmpty()) {
            username = claims.getSubject();
        }
        return username;
    }

    public Claims getClaims(String token) {
        String jwtToken = extractToken(token); // Bearer 제거 및 공백 처리
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        throw new IllegalArgumentException("Invalid Authorization header format");
    }

    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }
}
