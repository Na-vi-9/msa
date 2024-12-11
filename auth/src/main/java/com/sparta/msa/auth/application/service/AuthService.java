package com.sparta.msa.auth.application.service;

import com.sparta.msa.auth.presentation.exception.ErrorCode;
import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.domain.model.UserRoleEnum;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.response.SignInResponseDto;
import com.sparta.msa.auth.presentation.response.SignUpResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${userService.url}")
    private String userServiceUrl;


    @Value("${service.jwt.secret-key}")
    private final SecretKey secretKey;

    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(RestTemplate restTemplate, SecretKey secretKey) {
        this.restTemplate = restTemplate;
        this.secretKey = secretKey;
    }


// #############userService
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
//                       UserRepository userRepository,
//                       PasswordEncoder passwordEncoder) {
//        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


    public CommonResponse<String> signUp(SignUpRequestDto signUpRequestDto) {
        try{
            // 1. userService에 회원가입 요청&응답
            ResponseEntity<SignUpResponseDto> response = restTemplate.exchange(
                    userServiceUrl + "/user/signUp",
                    HttpMethod.POST,
                    new HttpEntity<>(signUpRequestDto),
                    SignUpResponseDto.class // 응답으로 받을 class
            );

            // 2. 회원가입 처리 결과 받기
            SignUpResponseDto createdUser = response.getBody();

            if(createdUser != null) {
                return CommonResponse.ofSuccess("회원 가입 성공");
            }
            else{
                return CommonResponse.ofError(ErrorCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            // 예외 발생시 오류 응답
            return CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR.getDescription());
        }
    }

// #############userService
//    private void checkDuplicateUsername(String username) {
//        if(userRepository.existsByUsername(username)){
//            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS, ErrorCode.USERNAME_ALREADY_EXISTS.getDescription());
//        }
//    }
//
//    private void checkDuplicateEmail(String email) {
//        if(userRepository.existsByEmail(email)){
//            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, ErrorCode.EMAIL_ALREADY_EXISTS.getDescription());
//        }
//    }
//
//    private void checkDuplicateSlackId(String slackId) {
//        if(userRepository.existsBySlackId(slackId)){
//            throw new CustomException(ErrorCode.SLACKID_ALREADY_EXISTS, ErrorCode.SLACKID_ALREADY_EXISTS.getDescription());
//        }
//    }


    public String createAccessToken(String username, UserRoleEnum userRoleEnum) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", userRoleEnum)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public CommonResponse<String> signIn(SignInRequestDto signInRequestDto) {
        try {
            // 1. userService에 로그인 요청&응답
            ResponseEntity<SignInResponseDto> response = restTemplate.exchange(
                    userServiceUrl + "user/SignIn",
                    HttpMethod.POST,
                    new HttpEntity<>(signInRequestDto),
                    SignInResponseDto.class
            );

            // 2. 결과 받기
            SignInResponseDto signInResponseDto = response.getBody();

            if(signInResponseDto != null) {
                // 3. 로그인 성공시, 토큰 생성
                String accessToken = createAccessToken(signInResponseDto.getUsername(), signInResponseDto.getRole());
                return CommonResponse.ofSuccess(accessToken);
            }
            else{
                return CommonResponse.ofError(ErrorCode.NOT_FOUND_USER);
            }
        }catch (Exception e) {
            return CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR.getDescription());
        }
    }
}