package com.sparta.msa.auth.presentation.controller;

import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.response.SignInResponseDto;
import com.sparta.msa.auth.presentation.response.SignUpResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "users", url = "${userService.url}")
public interface UserServiceClient {

    @PostMapping("/users/signUp")
    CommonResponse<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto);

    @PostMapping("/users/signIn")
    CommonResponse<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto);
}
