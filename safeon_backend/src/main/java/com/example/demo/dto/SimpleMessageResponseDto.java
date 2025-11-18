package com.example.demo.dto;

public record SimpleMessageResponseDto(String message) {
    public static SimpleMessageResponseDto of(String message) {
        return new SimpleMessageResponseDto(message);
    }
}
