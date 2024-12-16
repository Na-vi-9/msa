package com.sparta.msa.auth.infrastructure.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
