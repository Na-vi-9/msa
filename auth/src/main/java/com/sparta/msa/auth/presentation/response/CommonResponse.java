package com.sparta.msa.auth.presentation.response;

import com.sparta.msa.auth.presentation.exception.ErrorCode;
import lombok.Getter;

//@Getter
//public class CommonResponse<T> {
//
//    private final T data;
//    private final String message;
//
//    private CommonResponse(T data, String message) {
//        this.data = data;
//        this.message = message;
//    }
//    public static CommonResponse ofError(ErrorCode code) {
//        return new CommonResponse<>(null, code.getDescription());
//    }
//
//    public static <T> CommonResponse<T> ofSuccess(T data) {
//        return new CommonResponse<>(data, "SUCCESS");
//    }
//
//    public static CommonResponse<Void> ofError(String message) {
//        return new CommonResponse<>(null, message);
//    }
//}
public record CommonResponse<T>(
        T data,
        String message
) {

    public static <T>CommonResponse<T> ofError(ErrorCode code) {
        return new CommonResponse<>(null, code.getDescription());
    }

    public static <T>CommonResponse<T> ofError(String message) {
        return new CommonResponse<>(null, message);
    }


    public static <T>CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(data, "SUCCESS");
    }

}