package com.sparta.msa.auth.application.service;

import com.sparta.msa.auth.presentation.controller.UserServiceClient;
import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.response.SignInResponseDto;
import com.sparta.msa.auth.presentation.response.SignUpResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserServiceClient userServiceClient;

    @Autowired
    public AuthService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public CommonResponse<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {
        return userServiceClient.signUp(signUpRequestDto);
    }

    public CommonResponse<SignInResponseDto> signIn(SignInRequestDto signInRequestDto) {
        return userServiceClient.signIn(signInRequestDto);
    }
}