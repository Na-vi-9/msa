package com.sparta.msa.auth.infrastructure.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor("${JWT_SECRET_KEY}".getBytes());
    }
}
