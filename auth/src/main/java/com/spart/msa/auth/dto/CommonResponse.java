package com.spart.msa.auth.dto;

public record CommonResponse<T>(
        T data,
        String message
) {

    public static CommonResponse<Void> ofError(String message) {
        return new CommonResponse<>(null, message);
    }


    public static <T> CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(data, "SUCCESS");
    }
}