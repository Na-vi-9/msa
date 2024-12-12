package com.sparta.user.presentation.controller;

import com.sparta.user.application.service.UserService;
import com.sparta.user.presentation.request.UserSignInRequestDto;
import com.sparta.user.presentation.request.UserSignUpRequestDto;
import com.sparta.user.presentation.response.CommonResponse;
import com.sparta.user.presentation.response.UserSignInResponseDto;
import com.sparta.user.presentation.response.UserSignUpResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:19098")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public CommonResponse<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return userService.signUp(userSignUpRequestDto);
    }

    @PostMapping("/signIn")
    public CommonResponse<UserSignInResponseDto> signIn(@Valid @RequestBody UserSignInRequestDto userSignInRequestDto) {
        return userService.signIn(userSignInRequestDto);
    }
}
