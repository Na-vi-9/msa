package com.sparta.user.presentation.response;


import com.sparta.user.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignUpResponseDto {

    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;
    private UserRoleEnum role;
}
