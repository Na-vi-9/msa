package com.sparta.msa.auth.presentation.controller;


import com.sparta.msa.auth.application.service.AuthService;
import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.response.AuthResponseDto;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.domain.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public CommonResponse<User> signUp(@Valid @RequestBody SignUpRequestDto signupRequestDto) {
        User createdUser = authService.signUp(signupRequestDto);
        return CommonResponse.ofSuccess(createdUser);
    }

    @PostMapping("/signIn")
    public CommonResponse<AuthResponseDto> createAccessToken(@RequestBody SignInRequestDto signInRequestDto) {
        String token = authService.signIn(signInRequestDto.getUsername(), signInRequestDto.getPassword());
        return CommonResponse.ofSuccess(new AuthResponseDto(token));
    }
}
