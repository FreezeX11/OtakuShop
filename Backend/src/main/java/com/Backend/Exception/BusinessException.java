package com.Backend.Exception;

public class BusinessException extends IllegalStateException {
    public BusinessException(String message) {
        super(message);
    }
}
