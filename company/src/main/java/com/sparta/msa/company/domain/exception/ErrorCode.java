package com.sparta.msa.company.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "업체 UUID: %s를 찾을 수 없습니다."),
    DELETED_COMPANY(HttpStatus.BAD_REQUEST, "삭제된 업체입니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "작업을 수행하기 위해 필요한 권한이 없습니다."),
    USER_NAME_MISMATCH(HttpStatus.UNAUTHORIZED, "유저 이름이 일치하지 않습니다. 로그인 정보가 올바른지 확인해주세요.")
    ;

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }

}
