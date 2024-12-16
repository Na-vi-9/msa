package com.sparta.user.application.service;

import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.infrastructure.security.JwtTokenProvider;
import com.sparta.user.presentation.exception.CustomException;
import com.sparta.user.presentation.exception.ErrorCode;
import com.sparta.user.presentation.request.*;
import com.sparta.user.presentation.response.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // auth - 회원가입
    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) {

        // 1. 중복 확인
        checkDuplicateUsername(userSignUpRequestDto.getUsername());
        checkDuplicateEmail(userSignUpRequestDto.getEmail());
        checkDuplicateSlackId(userSignUpRequestDto.getSlackId());

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // 3. 회원가입 데이터 생성
        User user = User.builder()
                .username(userSignUpRequestDto.getUsername())
                .password(encodedPassword)
                .name(userSignUpRequestDto.getName())
                .email(userSignUpRequestDto.getEmail())
                .slackId(userSignUpRequestDto.getSlackId())
                .role(userSignUpRequestDto.getRole())
                .createdAt(LocalDateTime.now())
                .createdBy(userSignUpRequestDto.getUsername())
                .build();

        User savedUser = userRepository.save(user);

        // 4. 저장된 유저 정보를 UserResponseDto
        return UserSignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .password(encodedPassword)
                .email(savedUser.getEmail())
                .slackId(savedUser.getSlackId())
                .role(savedUser.getRole())
                .name(savedUser.getName())
                .createBy(savedUser.getCreatedBy())
                .createAt(savedUser.getCreatedAt())
                .isDeleted(savedUser.isDeleted())
                .build();
    }


    // auth - 로그인
    public UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto) {
        // 1. 사용자 존재 여부 확인
        User user = userRepository.findByUsername(userSignInRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(userSignInRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER, "비밀번호가 일치하지 않습니다.");
        }

        // 3. access token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().name());

        return new UserSignInResponseDto(user.getUsername(), user.getRole().name(), accessToken);
    }

    // 유저 생성
    @Transactional
    public UserSignUpResponseDto createUser(@Valid UserSignUpRequestDto userSignUpRequestDto, String token) {
        // 1. MASTER 권한 검증
        if (!isMasterRole(token)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }

        String jwtToken = jwtTokenProvider.extractToken(token);
        String tokenUsername = jwtTokenProvider.getUsernameFromToken(jwtToken);

        // 2. 중복 확인
        checkDuplicateUsername(userSignUpRequestDto.getUsername());
        checkDuplicateEmail(userSignUpRequestDto.getEmail());
        checkDuplicateSlackId(userSignUpRequestDto.getSlackId());

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // 4. 회원가입 데이터 생성
        User user = User.builder()
                .username(userSignUpRequestDto.getUsername())
                .password(encodedPassword)
                .name(userSignUpRequestDto.getName())
                .email(userSignUpRequestDto.getEmail())
                .slackId(userSignUpRequestDto.getSlackId())
                .role(userSignUpRequestDto.getRole())
                .createdAt(LocalDateTime.now())
                .createdBy(tokenUsername)
                .build();

        User savedUser = userRepository.save(user);

        // 5. 저장된 유저 정보를 UserSignUpResponseDto로 변환하여 반환
        return UserSignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .password(encodedPassword)
                .email(savedUser.getEmail())
                .slackId(savedUser.getSlackId())
                .role(savedUser.getRole())
                .name(savedUser.getName())
                .createBy(savedUser.getCreatedBy())
                .createAt(savedUser.getCreatedAt())
                .isDeleted(savedUser.isDeleted())
                .build();
    }

    // 권한 수정
    @Transactional
    public CommonResponse<UserRoleUpdateResponseDto> updateUserRole(String token,
                                                                    String username,
                                                                    UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        // 1. MASTER 권한 검증
        if (!isMasterRole(token)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }
        String jwtToken = jwtTokenProvider.extractToken(token);
        String tokenUsername = jwtTokenProvider.getUsernameFromToken(jwtToken);

        // 2. 사용자 존재 여부 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 3. role 업데이트
        user.updateRole(userRoleUpdateRequestDto.getRole(), tokenUsername);

        userRepository.save(user);

        UserRoleUpdateResponseDto userRoleUpdateResponseDto = UserRoleUpdateResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();

        return CommonResponse.ofSuccess(userRoleUpdateResponseDto);
    }

    // 유저 단건 조회
    public CommonResponse<UserRoleInfoResponseDto> getUserInfoByUsername(String token, String username) {
        // 1. 토큰에서 username 추출
        String tokenUsername = jwtTokenProvider.getUsernameFromToken(token);

        // 2. 자기 자신이거나 MASTER 권한이 있는지 확인
        if (!tokenUsername.equals(username) && !isMasterRole(token)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }

        // 3. 유저 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return CommonResponse.ofSuccess(new UserRoleInfoResponseDto(user.getUsername(), user.getRole()));
    }

    // 유저 정보 수정
    @Transactional
    public CommonResponse<UserInfoUpdateResponseDto> updateUserInfo(String token, String username, UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        // 1. MASTER 권한 검증
        if (!isMasterRole(token) && !jwtTokenProvider.getUsernameFromToken(token).equals(username)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }

        // 2. 사용자 존재 여부 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(userInfoUpdateRequestDto.getPassword());

        // 3. 유저 정보 수정
        user.updateUserInfo(userInfoUpdateRequestDto, encodedPassword, jwtTokenProvider.getUsernameFromToken(token));

        User userSaved = userRepository.save(user);

        UserInfoUpdateResponseDto userInfoUpdateResponseDto = new UserInfoUpdateResponseDto(
                userSaved.getUsername(),
                userSaved.getPassword(),
                userSaved.getName(),
                userSaved.getEmail(),
                userSaved.getSlackId(),
                userSaved.getUpdatedBy(),
                userSaved.getUpdatedAt()
        );
        return CommonResponse.ofSuccess(userInfoUpdateResponseDto);
    }

    // 유저 삭제
    public CommonResponse<UserDeleteResponseDto> deleteUser(String token, String username, UserDeleteRequestDto userDeleteRequestDto) {

        // 1. MASTER 권한 검증
        if (!isMasterRole(token) && !jwtTokenProvider.getUsernameFromToken(token).equals(username)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }

        // 2. 사용자 존재 여부 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 3. 이미 삭제된 유저인지 확인
        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.USER_ALREADY_DELETED, "이미 삭제된 유저입니다.");
        }

        // 4. 유저 삭제
        user.markAsDeleted(jwtTokenProvider.getUsernameFromToken(token));
        userRepository.save(user); // 유저 정보 업데이트

        UserDeleteResponseDto userDeleteResponseDto = new UserDeleteResponseDto(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getSlackId(),
                user.getDeletedAt(),
                user.getDeletedBy(),
                user.isDeleted()
        );

        return CommonResponse.ofSuccess(userDeleteResponseDto);
    }


    // 유저 목록 조회
    public Page<UserSearchResponseDto> getUsers(String condition, String keyword, Pageable pageable, String token) {

        // 1. MASTER 권한 검증
        if (!isMasterRole(token)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "권한이 없습니다.");
        }
        // 2. QueryDsl
        return userRepository.findUsersWithConditions(condition, keyword, pageable);
    }









    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public boolean isMasterRole(String token) {
        String jwtToken = jwtTokenProvider.extractToken(token);
        String role = jwtTokenProvider.getRoleFromToken(jwtToken);
        return "MASTER".equals(role);
    }

    public String getSlackIdByUsername(String username) {
        // 사용자 존재 여부 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return user.getSlackId();
    }

}
