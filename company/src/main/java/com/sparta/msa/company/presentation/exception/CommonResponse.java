package com.sparta.msa.company.presentation.exception;

import com.sparta.msa.company.domain.exception.ErrorCode;

public record CommonResponse<T>(
        T data,
        String message
) {

    public static CommonResponse<Void> ofError(ErrorCode code) {
        return new CommonResponse<>(null, code.getDescription());
    }

    public static CommonResponse<Void> ofError(String message) {
        return new CommonResponse<>(null, message);
    }

    public static <T> CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(data, "SUCCESS");
    }

}