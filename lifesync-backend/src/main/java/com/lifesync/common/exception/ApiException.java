package com.lifesync.common.exception;

import org.springframework.http.HttpStatus;

/** Base runtime exception carrying an HTTP status, so the handler can map it directly. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
