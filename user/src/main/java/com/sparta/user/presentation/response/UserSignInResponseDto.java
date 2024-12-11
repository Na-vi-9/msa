package com.sparta.user.presentation.response;

import com.sparta.user.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInResponseDto {
    private String username;
    private String role;
    private String accessToken;
}
