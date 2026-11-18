package com.essadany.localadvisor.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        int status,
        String code,
        String message,
        String path,
        boolean retryable,
        Instant timestamp
) {
    public static ApiError of(HttpStatus status, String code, String message, String path) {
        return new ApiError(status.value(), code, message, path, status.is5xxServerError(), Instant.now());
    }
}
