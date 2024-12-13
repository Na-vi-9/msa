package com.sparta.user.presentation.response;

import com.sparta.user.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateResponseDto {
    private String username;
    private UserRoleEnum role;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
