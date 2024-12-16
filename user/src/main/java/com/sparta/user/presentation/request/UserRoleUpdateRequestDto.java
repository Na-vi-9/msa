package com.sparta.user.presentation.request;

import com.sparta.user.domain.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateRequestDto {
    private String username;
    private UserRoleEnum role;
}
