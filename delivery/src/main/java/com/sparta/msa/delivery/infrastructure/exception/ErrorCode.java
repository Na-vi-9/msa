package com.sparta.msa.delivery.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업에 대한 권한이 없습니다."),

    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."),
    DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 담당자를 찾을 수 없습니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 허브 ID입니다."),
    DUPLICATE_DELIVERY_MANAGER(HttpStatus.CONFLICT, "이미 존재하는 배송 담당자입니다."),
    DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 경로 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
