package com.lailsonbento.transferenciaapp.exceptions.config;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiErrorResponse(String message, int status, String error, String path, ZonedDateTime timestamp) {

    public ApiErrorResponse(String message, int status, String path) {
        this(message, status, HttpStatus.valueOf(status).getReasonPhrase(), path, ZonedDateTime.now());
    }
}
