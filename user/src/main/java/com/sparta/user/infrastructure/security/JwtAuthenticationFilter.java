package com.sparta.user.infrastructure.security;

import com.sparta.user.domain.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.getTokenFromRequest(request);

        if (token != null && !jwtTokenProvider.isTokenExpired(token)) {

            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 인증 객체 생성
            Authentication authentication = createAuthentication(username, token);

            // SecurityContext에 인증 객체 설정
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }


    private Authentication createAuthentication(String username, String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // 사용자 정보 로드

        String role = jwtTokenProvider.getRoleFromToken(token);  // 토큰에서 권한 추출
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));  // 권한 설정

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }



}