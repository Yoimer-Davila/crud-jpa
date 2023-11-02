package com.crudjpa.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public record ErrorMessage(int code, String message, String description,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") LocalDateTime date) {
    public ErrorMessage(int code, String message, String description, LocalDateTime date) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.date = date;
    }

    public static ErrorMessage withCode(int code, Exception e, WebRequest request) {
        return new ErrorMessage(code, e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());
    }

    public static ErrorMessage badRequestError(Exception e, WebRequest request) {
        return withCode(HttpStatus.BAD_REQUEST.value(), e, request);
    }

    public static ErrorMessage internalServerError(Exception e, WebRequest request) {
        return withCode(HttpStatus.INTERNAL_SERVER_ERROR.value(), e, request);
    }

    public static ErrorMessage notFoundError(Exception e, WebRequest request) {
        return withCode(HttpStatus.NOT_FOUND.value(), e, request);
    }
}
