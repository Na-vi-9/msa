package com.sparta.msa.order.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args));
        this.errorCode = errorCode;
    }
}
