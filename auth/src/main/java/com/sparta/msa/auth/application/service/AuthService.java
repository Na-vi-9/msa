package com.sparta.msa.auth.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa.auth.presentation.request.SignInRequestDto;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import com.sparta.msa.auth.presentation.response.CommonResponse;
import com.sparta.msa.auth.presentation.response.SignInResponseDto;
import com.sparta.msa.auth.presentation.response.SignUpResponseDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import javax.crypto.SecretKey;
import java.io.IOException;

@Slf4j
@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${userService.url}")
    private String userServiceUrl;



    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // 회원가입 처리
    public CommonResponse<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {
        try {
            // 1. user 서비스에 회원가입 요청
            ResponseEntity<CommonResponse<SignUpResponseDto>> response = restTemplate.exchange(
                    userServiceUrl + "/user/signUp",
                    HttpMethod.POST,
                    new HttpEntity<>(signUpRequestDto),
                    new ParameterizedTypeReference<CommonResponse<SignUpResponseDto>>() {}
            );

            // 응답 확인 후, 성공 시 반환
            if (response.getBody() != null && "SUCCESS".equals(response.getBody().message())) {
                return CommonResponse.ofSuccess(response.getBody().data());
            }

            return CommonResponse.ofError(response.getBody().message());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // user 서비스 요청 중 예외가 발생하면, 예외 메시지를 정리해서 전달
            log.error("User Service 요청 실패: {}", e.getMessage(), e);

            // 에러 응답 메시지를 정리해서 반환
            // {
            //    "data": null,
            //    "message": "400  on POST request for \"http://localhost:19097/user/signUp\": \"{\"data\":null,\"message\":\"사용자 아이디가 이미 존재 합니다.\"}\""
            //}
            String errorMessage = extractErrorMessage(e);

            return CommonResponse.ofError(errorMessage);

        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage(), e);
            return CommonResponse.ofError("회원가입 처리 중 오류 발생");
        }
    }

    // 에러 메시지를 추출하는 유틸리티 메서드
    private String extractErrorMessage(Exception e) {
        // HTTP 상태 코드가 400번대나 500번대이면, 본문에서 유효한 메시지를 추출
        if (e instanceof HttpClientErrorException || e instanceof HttpServerErrorException) {
            try {
                String responseBody = ((HttpStatusCodeException) e).getResponseBodyAsString();
                // JSON 형태로 응답을 받았으므로, 본문에서 메시지 추출
                // {"data":null,"message":"사용자 아이디가 이미 존재 합니다."}
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                return rootNode.path("message").asText();  // message에서 메시지 추출
            } catch (IOException ioException) {
                log.error("오류 메시지 추출 실패: {}", ioException.getMessage());
                return "회원가입 처리 중 오류 발생";
            }
        }
        return e.getMessage();
    }



    public CommonResponse<SignInResponseDto> signIn(SignInRequestDto signInRequestDto) {
        try {
            // 1. user 서비스에 로그인 요청
            ResponseEntity<CommonResponse<SignInResponseDto>> response = restTemplate.exchange(
                    userServiceUrl + "/user/signIn",
                    HttpMethod.POST,
                    new HttpEntity<>(signInRequestDto),
                    new ParameterizedTypeReference<CommonResponse<SignInResponseDto>>() {}
            );

            // 응답 확인 후, 성공 시 반환
            if (response.getBody() != null && "SUCCESS".equals(response.getBody().message())) {
                return CommonResponse.ofSuccess(response.getBody().data());
            }

            return CommonResponse.ofError(response.getBody().message());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("User Service 요청 실패 : {}", e.getMessage(), e);

            // 에러 응답 메시지를 정리해서 반환
            String errorMessage = extractErrorMessage(e);
            return CommonResponse.ofError(errorMessage);

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생 : {}", e.getMessage(), e);
            return CommonResponse.ofError("로그인 처리 중 오류 발생");
        }
    }
}