package com.task.test.clearsolutions.exception;

public class Status409UserAlreadyRegisteredException extends RuntimeException {
    public Status409UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public Status409UserAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
