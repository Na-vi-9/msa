package com.sparta.user.infrastructure.config;

import com.sparta.user.domain.service.UserDetailsServiceImpl;
import com.sparta.user.infrastructure.security.JwtAuthenticationFilter;
import com.sparta.user.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.POST,"/users/signIn").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users/signUp").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").hasAnyAuthority("MASTER")
                        .requestMatchers(HttpMethod.PUT,"/users/authority/{username}").hasAnyAuthority("MASTER")
                        .requestMatchers(HttpMethod.GET, "/users/info").hasAnyAuthority("MASTER")
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("MASTER")
                        .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAnyAuthority("MASTER", "HUB_MANAGER", "COMPANY_MANAGER", "DELIVERY_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasAnyAuthority("MASTER")

                        // 그 외의 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}