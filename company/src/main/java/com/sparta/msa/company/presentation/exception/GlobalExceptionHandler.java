package com.sparta.msa.company.presentation.exception;

import com.sparta.msa.company.domain.exception.CustomException;
import com.sparta.msa.company.domain.exception.ErrorCode;
import jakarta.persistence.QueryTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Custom Exception */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponse<Void>> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getErrorCode());
        return new ResponseEntity<>(CommonResponse.ofError(e.getErrorCode()), e.getErrorCode().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> methodArgumentValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage= String.format(ErrorCode.BAD_REQUEST.getDescription(), errors);

        return new ResponseEntity<>(CommonResponse.ofError(errorMessage), ErrorCode.BAD_REQUEST.getStatus());
    }


    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<CommonResponse<Void>> handleQueryTimeoutException(QueryTimeoutException e) {
        log.error("fail to execute query, {}", e.getMessage());
        return new ResponseEntity<>(CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<CommonResponse<Void>> handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(CommonResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    /* Security 사용시 추가
    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(final AuthorizationDeniedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.FORBIDDEN), ErrorCode.FORBIDDEN.getStatus());
    }
    */

}