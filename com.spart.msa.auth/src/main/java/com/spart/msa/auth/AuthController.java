package com.spart.msa.auth;


import com.spart.msa.auth.dto.SignInRequestDto;
import com.spart.msa.auth.dto.AuthResponseDto;
import com.spart.msa.auth.dto.CommonResponse;
import com.spart.msa.auth.dto.SignUpRequestDto;
import com.spart.msa.auth.entity.User;

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
