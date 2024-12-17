package com.sparta.msa.company.domain.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message; // 동적 메시지

    public CustomException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args));
        this.errorCode = errorCode;
        this.message = super.getMessage();
    }
}
