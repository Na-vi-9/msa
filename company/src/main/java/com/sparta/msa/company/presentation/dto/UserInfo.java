package com.sparta.msa.company.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private String token;
    private String username;
    private String role;

    public static UserInfo of(String token, String username, String role) {
        return UserInfo.builder()
                .token(token)
                .username(username)
                .role(role)
                .build();
    }
}
