package com.Backend.Exception;

import com.Backend.Payload.Response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return new ApiError(
                "Resource Not Found",
                404,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExist(ResourceAlreadyExistException ex, HttpServletRequest req) {
        return new ApiError(
                "Resource Already Exist",
                409,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(ResourceInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInUse(ResourceInUseException ex, HttpServletRequest req) {
        return new ApiError(
                "Resource Is Used",
                409,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBusiness(BusinessException ex, HttpServletRequest req) {
        return new ApiError(
                "Business Rule Violation",
                400,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        return new ApiError(
                "Validation Error",
                400,
                message,
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return new ApiError(
                "Invalid Credentials",
                401,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(DisabledAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadDisableAccount(DisabledAccountException ex, HttpServletRequest req) {
        return new ApiError(
                "Unauthorized",
                401,
                ex.getMessage(),
                req.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneric(Exception ex, HttpServletRequest req) {
        return new ApiError(
                "Internal Server Error",
                500,
                "An unexpected error occurred",
                req.getRequestURI(),
                Instant.now()
        );
    }

}
