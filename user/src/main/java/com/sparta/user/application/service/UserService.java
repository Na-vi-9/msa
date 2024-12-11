package com.sparta.user.application.service;

import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.infrastructure.security.JwtSecurity;
import com.sparta.user.presentation.exception.CustomException;
import com.sparta.user.presentation.exception.ErrorCode;
import com.sparta.user.presentation.request.UserSignUpRequestDto;
import com.sparta.user.presentation.response.CommonResponse;

import com.sparta.user.presentation.response.UserSignUpResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurity jwtSecurity;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtSecurity jwtSecurity) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecurity = jwtSecurity;
    }

    // 회원가입 처리
    @Transactional
    public CommonResponse<UserSignUpResponseDto> signUp(UserSignUpRequestDto usersignUpRequestDto) {
        try {

            // 1. 중복 확인
            checkDuplicateUsername(usersignUpRequestDto.getUsername());
            checkDuplicateEmail(usersignUpRequestDto.getEmail());
            checkDuplicateSlackId(usersignUpRequestDto.getSlackId());

            // 2. 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(usersignUpRequestDto.getPassword());

            // 3. 회원가입 데이터 생성
            User user = User.builder()
                    .username(usersignUpRequestDto.getUsername())
                    .password(encodedPassword)
                    .name(usersignUpRequestDto.getName())
                    .email(usersignUpRequestDto.getEmail())
                    .slackId(usersignUpRequestDto.getSlackId())
                    .role(usersignUpRequestDto.getRole())
                    .createdBy(usersignUpRequestDto.getUsername())
                    .build();


            User savedUser = userRepository.save(user);

            // 4. 저장된 유저 정보를 UserResponseDto로 변환하여 반환
            UserSignUpResponseDto userSignUpResponseDto = UserSignUpResponseDto.builder()
                    .username(savedUser.getUsername())
                    .password(encodedPassword)
                    .email(savedUser.getEmail())
                    .slackId(savedUser.getSlackId())
                    .role(savedUser.getRole())
                    .name(savedUser.getName())
                    .build();

            // 5. 성공 응답
            return CommonResponse.ofSuccess(userSignUpResponseDto);
        }  catch (CustomException e) {
        // CustomException 예외 처리
        log.error("회원가입 중 예외 발생: {}", e.getMessage(), e);
        throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리 중 오류 발생");
        }
    }





/////////////////////////////////////////////////////////////////////////////
    private void checkDuplicateUsername(String username) {
    if(userRepository.existsByUsername(username)){
        throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS, ErrorCode.USERNAME_ALREADY_EXISTS.getDescription());
        }
    }

    private void checkDuplicateEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, ErrorCode.EMAIL_ALREADY_EXISTS.getDescription());
        }
    }

    private void checkDuplicateSlackId(String slackId) {
        if(userRepository.existsBySlackId(slackId)){
            throw new CustomException(ErrorCode.SLACKID_ALREADY_EXISTS, ErrorCode.SLACKID_ALREADY_EXISTS.getDescription());
        }
    }
}
