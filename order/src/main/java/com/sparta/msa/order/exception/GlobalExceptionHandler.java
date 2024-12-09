package com.sparta.msa.order.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.QueryTimeoutException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> handleCustomException(CustomException e) {
        log.error("Custom Exception: {}", e.getMessage());
        return new ResponseEntity<>(CommonResponse.ofError(e.getMessage()), e.getErrorCode().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGlobalException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(CommonResponse.ofError("서버 내부 오류가 발생했습니다."), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = String.format(ErrorCode.BAD_REQUEST.getDescription(), errors);
        return new ResponseEntity<>(CommonResponse.ofError(errorMessage), ErrorCode.BAD_REQUEST.getStatus());
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<CommonResponse<Void>> handleQueryTimeoutException(QueryTimeoutException e) {
        log.error("Query Timeout: {}", e.getMessage());
        return new ResponseEntity<>(CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR.getDescription()), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<CommonResponse<Void>> handleRuntimeException(final RuntimeException e) {
        log.error("Runtime Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR.getDescription()), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
