package com.lailsonbento.transferenciaapp.exceptions.config;

import com.lailsonbento.transferenciaapp.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        var message = String.join(",\n", errors);
        return getApiErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler({ AccountException.class, AccountNotFoundException.class, InsufficientBalanceException.class })
    public ResponseEntity<ApiErrorResponse> handleAccountException(Exception ex, HttpServletRequest request) {
        return getApiErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler({ TransactionException.class })
    public ResponseEntity<ApiErrorResponse> handleTransactionException(Exception ex, HttpServletRequest request) {
        return getApiErrorResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler({ TransactionNotAuthorizedException.class })
    public ResponseEntity<ApiErrorResponse> handleTransactionNotAuthorizedException(Exception ex, HttpServletRequest request) {
        return getApiErrorResponseResponseEntity(HttpStatus.FORBIDDEN, ex, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return getApiErrorResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ApiErrorResponse> getApiErrorResponseResponseEntity(HttpStatus status, Exception ex, HttpServletRequest request) {
        var apiError = new ApiErrorResponse(ex.getMessage(), status.value(), request.getServletPath());
        return new ResponseEntity<>(apiError, status);
    }

    private ResponseEntity<ApiErrorResponse> getApiErrorResponseResponseEntity(HttpStatus status, String message, HttpServletRequest request) {
        var apiError = new ApiErrorResponse(message, status.value(), request.getServletPath());
        return new ResponseEntity<>(apiError, status);
    }
}