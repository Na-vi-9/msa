package com.sparta.msa.auth.presentation.response;

import com.sparta.msa.auth.domain.model.UserRoleEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {

    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;
    private UserRoleEnum role;
}