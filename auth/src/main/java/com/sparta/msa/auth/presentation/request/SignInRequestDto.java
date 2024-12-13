package com.sparta.msa.auth.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {
    private String username;
    private String password;
}