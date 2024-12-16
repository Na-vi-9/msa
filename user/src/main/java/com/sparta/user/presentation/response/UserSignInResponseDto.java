package com.sparta.user.presentation.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInResponseDto {
    private String username;
    private String role;
    private String accessToken;
}
