package com.sparta.msa.hub.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_FOUND_HUB(HttpStatus.NOT_FOUND, "허브 UUID: %s를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }

}
