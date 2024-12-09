package com.sparta.msa.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통 예외
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),

    // 주문 관련 예외
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다. (주문 ID: %s)"),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다. (주문 ID: %s)"),
    ORDER_CANNOT_BE_UPDATED(HttpStatus.BAD_REQUEST, "수정할 수 없는 주문입니다. (주문 ID: %s)"),
    ORDER_CREATION_FAILED(HttpStatus.BAD_REQUEST, "주문 생성에 실패했습니다. (사유: %s)"),
    INVALID_ORDER_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 요청입니다."),

    // 상품 관련 예외
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다. (상품 ID: %s)"),
    PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다. (상품 ID: %s)"),

    // 업체 관련 예외
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다. (업체 ID: %s)"),
    SUPPLIER_NOT_FOUND(HttpStatus.NOT_FOUND, "공급 업체를 찾을 수 없습니다. (업체 ID: %s)"),
    RECEIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "수령 업체를 찾을 수 없습니다. (업체 ID: %s)"),

    // 배송 관련 예외
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다. (배송 ID: %s)"),
    DELIVERY_CANNOT_BE_UPDATED(HttpStatus.BAD_REQUEST, "수정할 수 없는 배송 상태입니다. (배송 ID: %s)"),
    DELIVERY_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 배송입니다. (배송 ID: %s)");

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
