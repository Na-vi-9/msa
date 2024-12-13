package com.sparta.msa.auth.presentation.response;

import com.sparta.msa.auth.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {

    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;
    private UserRoleEnum role;

    private String createBy;
    private LocalDateTime createAt;
    private boolean isDeleted;
}