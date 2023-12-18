package com.websystique.exception;

public class StudentAlreadyExistException extends RuntimeException {

    public StudentAlreadyExistException() {
        super();
    }

    public StudentAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StudentAlreadyExistException(final String message) {
        super(message);
    }

    public StudentAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
