package com.example.demo.dto;

public record ApiResponseDto<T>(
        boolean success,
        T data
) {
    public static <T> ApiResponseDto<T> ok(T data) {
        return new ApiResponseDto<>(true, data);
    }

    public static ApiResponseDto<Void> ok() {
        return new ApiResponseDto<>(true, null);
    }

    public static <T> ApiResponseDto<T> fail(T data) {
        return new ApiResponseDto<>(false, data);
    }
}
