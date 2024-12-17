package com.sparta.msa.hub.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_FOUND_HUB(HttpStatus.NOT_FOUND, "허브 UUID: %s를 찾을 수 없습니다."),
    DELETED_HUB(HttpStatus.BAD_REQUEST, "이미 삭제된 허브입니다."),
    NOT_FOUND_HUB_ROUTE(HttpStatus.NOT_FOUND, "해당하는 허브 루트를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "작업을 수행하기 위해 필요한 권한이 없습니다.")
    ;

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }

}
