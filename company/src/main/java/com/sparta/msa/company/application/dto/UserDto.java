package com.sparta.msa.company.application.dto;

import com.sparta.msa.company.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private Role role;
}
