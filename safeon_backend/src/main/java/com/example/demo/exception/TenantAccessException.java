package com.example.demo.exception;

public class TenantAccessException extends RuntimeException {
    public TenantAccessException(String message) {
        super(message);
    }
}
