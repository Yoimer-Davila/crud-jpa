package com.crudjpa.exception;

public class CreateResourceValidationException extends RuntimeException {
    public CreateResourceValidationException() {}
    public CreateResourceValidationException(String message) {
        super(message);
    }
}
