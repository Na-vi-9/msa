package com.sparta.msa.order.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationUtils {

    private final JwtTokenProvider jwtTokenProvider;

    // 권한 검증
    public void validateRole(String token, String... allowedRoles) {
        String role = jwtTokenProvider.getRoleFromToken(token);
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(role)) {
                return;
            }
        }
        throw new IllegalArgumentException("접근 권한이 없습니다.");
    }

    // 토큰에서 username 추출
    public String extractUsername(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }
}
