package com.sparta.msa.auth.presentation.controller;


import com.sparta.msa.auth.application.service.AuthService;
import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public CommonResponse<String> SignUp(@Valid @RequestBody SignUpRequestDto signupRequestDto) {

        return authService.signUp(signupRequestDto);
    }

    @PostMapping("/signIn")
    public CommonResponse<String> createAccessToken(@RequestBody SignInRequestDto signInRequestDto) {
        return authService.signIn(signInRequestDto);
    }
}
