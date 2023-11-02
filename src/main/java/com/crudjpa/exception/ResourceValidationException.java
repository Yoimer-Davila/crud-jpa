package com.crudjpa.exception;

public class ResourceValidationException extends RuntimeException {
    public ResourceValidationException() {}
    public ResourceValidationException(String message) { super(message); }
}
