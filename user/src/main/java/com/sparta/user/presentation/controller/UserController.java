package com.sparta.user.presentation.controller;

import com.sparta.user.application.service.UserService;
import com.sparta.user.domain.model.User;
import com.sparta.user.presentation.exception.CustomException;
import com.sparta.user.presentation.exception.ErrorCode;
import com.sparta.user.presentation.request.*;
import com.sparta.user.presentation.response.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:19098")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // auth-회원가입
    @PostMapping("/signUp")
    public CommonResponse<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        try {
            UserSignUpResponseDto responseDto = userService.signUp(userSignUpRequestDto);
            return CommonResponse.ofSuccess(responseDto);

        } catch (CustomException e) {
            log.error("회원가입 중 예외 발생: {}", e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리 중 오류 발생");
        }
    }

    // auth-로그인
    @PostMapping("/signIn")
    public CommonResponse<UserSignInResponseDto> signIn(@Valid @RequestBody UserSignInRequestDto userSignInRequestDto) {
        try {
            UserSignInResponseDto responseDto = userService.signIn(userSignInRequestDto);

            return CommonResponse.ofSuccess(responseDto);

        } catch (CustomException e) {
            log.error("로그인 중 예외 발생: {}", e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "로그인 처리 중 오류 발생");
        }
    }

    // MASTER 권한-user 생성
    @PostMapping
    public CommonResponse<UserSignUpResponseDto> createUser(@RequestHeader("Authorization") String token,
                                                            @Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        try {
            UserSignUpResponseDto responseDto = userService.createUser(userSignUpRequestDto, token);
            return CommonResponse.ofSuccess(responseDto);
        } catch (CustomException e) {
            log.error("회원가입 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리 중 오류 발생");
        }
    }

    // MASTER 권한-유저 권한 수정
    @PatchMapping("/authority/{username}")
    public CommonResponse<UserRoleUpdateResponseDto> updateUserRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String username,
            @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        try {
            return userService.updateUserRole(token, username, userRoleUpdateRequestDto);
        } catch (CustomException e) {
            log.error("사용자 역할 변경 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("사용자 역할 변경 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 역할 변경 처리 중 오류 발생");
        }
    }

    // 유저 조회
    @GetMapping("/info")
    public CommonResponse<UserRoleInfoResponseDto> getUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestParam String username) {
        try {
            return userService.getUserInfoByUsername(token, username);
        } catch (CustomException e) {
            log.error("사용자 정보 조회 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("사용자 정보 조회 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 정보 조회 처리 중 오류 발생");
        }
    }

    // 유저 정보 수정
    @PutMapping("/{username}")
    public CommonResponse<UserInfoUpdateResponseDto> updateUserInfo(@RequestHeader("Authorization") String token,
                                                                    @PathVariable String username,
                                                                    @RequestBody UserInfoUpdateRequestDto userInfoUpdateRequest) {
        try {
            return userService.updateUserInfo(token, username, userInfoUpdateRequest);
        } catch (CustomException e) {
            log.error("유저 정보 수정 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("유저 정보 수정 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "유저 정보 수정 처리 중 오류 발생");
        }
    }

    // 유저 목록 전체 조회&검색
    @GetMapping
    public CommonResponse<Page<UserSearchResponseDto>> getUsers(
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "keyword", required = false) String keyword,
            Pageable pageable,
            @RequestHeader("Authorization") String token) {

        try {
            Page<UserSearchResponseDto> users = userService.getUsers(condition, keyword, pageable, token);

            return new CommonResponse<>(users, "SUCCESS");

        } catch (CustomException e) {
            log.error("유저 목록 조회 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("유저 목록 조회 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "유저 목록 조회 처리 중 오류 발생");
        }
    }

    // 유저 삭제
    @DeleteMapping("/{username}")
    public CommonResponse<UserDeleteResponseDto> deletedUser(@RequestHeader("Authorization") String token,
                                                             @PathVariable String username,
                                                             @RequestBody UserDeleteRequestDto userDeleteRequestDto) {
        try {
            return userService.deleteUser(token, username, userDeleteRequestDto);
        } catch (CustomException e) {
            log.error("유저 삭제 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("유저 삭제 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "유저 삭제 처리 중 오류 발생");
        }
    }

    @GetMapping("/slack-id")
    public String getSlackIdByUsername(@RequestParam("username") String username,
                                       @RequestHeader("Authorization") String token) {
        System.out.println("Received username: " + username);
        System.out.println("Received Authorization: " + token);

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }        // 권한 검증 없이 Slack ID 조회
        return userService.getSlackIdByUsername(username);
    }
}
