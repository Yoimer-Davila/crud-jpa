package com.crudjpa.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class HttpStatusCheckCode {

    private final HttpStatusCode code;
    private HttpStatusCheckCode(HttpStatusCode code) {
        this.code = code;
    }

    public static <Ty> HttpStatusCheckCode from(ResponseEntity<Ty> response) {
        return new HttpStatusCheckCode(response.getStatusCode());
    }

    public static HttpStatusCheckCode from(HttpStatusCode code) {
        return new HttpStatusCheckCode(code);
    }

    public boolean is(HttpStatusCode code) {
        return this.code.isSameCodeAs(code);
    }

    public boolean isOk() {
        return is(HttpStatus.OK);
    }

    public boolean isCreated() {
        return is(HttpStatus.CREATED);
    }

    public boolean isNotFound() {
        return is(HttpStatus.NOT_FOUND);
    }

    public boolean isBadRequest() {
        return is(HttpStatus.BAD_REQUEST);
    }

    public boolean isInternalServerError() {
        return is(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
