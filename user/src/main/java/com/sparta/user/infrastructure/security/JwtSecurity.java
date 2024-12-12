package com.sparta.user.infrastructure.security;

import com.sparta.user.domain.model.UserRoleEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtSecurity {  // 클래스 이름 변경

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    public String createAccessToken(String username, UserRoleEnum userRoleEnum) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", userRoleEnum.name())  // role을 Enum의 이름으로 저장
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512) // 서명
                .compact(); // JWT 반환
    }
}