package com.sparta.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateResponseDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String slackId;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
