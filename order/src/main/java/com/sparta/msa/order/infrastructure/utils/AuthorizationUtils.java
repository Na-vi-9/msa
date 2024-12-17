package com.sparta.msa.order.infrastructure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtils {

    @Value("${service.jwt.secret-key}")
    private String jwtSecretKey;


    private final JwtTokenProvider jwtTokenProvider;

    // 생성자 주입
    public AuthorizationUtils(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

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

    public void validateRole(String token, String[] roles) {
        // 토큰에서 역할 추출
        String role = jwtTokenProvider.getRoleFromToken(token);

        // roles 배열에 역할이 포함되어 있는지 확인
        boolean isValidRole = false;
        for (String allowedRole : roles) {
            if (allowedRole.equals(role)) {
                isValidRole = true;
                break;  // 역할이 일치하면 루프 종료
            }
        }

        // 역할이 일치하지 않으면 예외 발생
        if (!isValidRole) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }
}
