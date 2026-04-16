package com.Backend.Exception;

public class StripeException extends RuntimeException {
    public StripeException(String message, Throwable cause) {
        super(message, cause);
    }
}
