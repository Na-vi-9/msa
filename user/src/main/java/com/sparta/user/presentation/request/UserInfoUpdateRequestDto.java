package com.sparta.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;
}
