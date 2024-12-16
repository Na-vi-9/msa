package com.sparta.msa.ai.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "사용자 아이디가 이미 존재 합니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이메일이 이미 존재 합니다."),
    SLACKID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "슬랙 아이디가 이미 존재합니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "로그인 정보가 잘못 되었습니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "접근이 거부 되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 사용자 입니다.");

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
