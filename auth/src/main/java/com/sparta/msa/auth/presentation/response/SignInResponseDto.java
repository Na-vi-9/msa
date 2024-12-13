package com.sparta.msa.auth.presentation.response;

import com.sparta.msa.auth.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {
    private String username;
    private UserRoleEnum role;
    private String accessToken;
}
