package com.sparta.msa.order.exception;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final T data;
    private final String message;

    private CommonResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(data, "SUCCESS");
    }

    public static CommonResponse<Void> ofError(String message) {
        return new CommonResponse<>(null, message);
    }
}
