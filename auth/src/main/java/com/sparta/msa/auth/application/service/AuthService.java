package com.sparta.msa.auth.application.service;

import com.sparta.msa.auth.domain.repository.UserRepository;
import com.sparta.msa.auth.presentation.exception.CustomException;
import com.sparta.msa.auth.presentation.exception.ErrorCode;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.domain.model.UserRoleEnum;
import com.sparta.msa.auth.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User signUp(SignUpRequestDto signUpRequestDto) {
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // 아이디 중복 확인
        checkDuplicateUsername(signUpRequestDto.getUsername());
        // 이메일 중복 확인
        checkDuplicateEmail(signUpRequestDto.getEmail());
        // slackId 중복 확인
        checkDuplicateSlackId(signUpRequestDto.getSlackId());

        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(encodedPassword)
                .name(signUpRequestDto.getUsername())
                .email(signUpRequestDto.getEmail())
                .slackId(signUpRequestDto.getSlackId())
                .role(signUpRequestDto.getRole())
                .createdBy(signUpRequestDto.getUsername())
                .build();

        return userRepository.save(user);
    }


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

    public String createAccessToken(String username, String password, UserRoleEnum userRoleEnum) {
        return Jwts.builder()
                .claim("username", username)
                .claim("password", password)
                .claim("role", userRoleEnum)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String signIn(String username, String password) {
        User user = userRepository.findByusername(username)
                .orElseThrow(()->
                        new CustomException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME.getDescription()));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH, ErrorCode.PASSWORD_MISMATCH.getDescription());
        }
        return createAccessToken(user.getUsername(), user.getPassword(), user.getRole());
    }
}