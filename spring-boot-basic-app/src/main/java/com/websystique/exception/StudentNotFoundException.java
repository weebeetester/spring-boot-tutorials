package com.websystique.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Could not find student " + id);
    }

    public StudentNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StudentNotFoundException(final String message) {
        super(message);
    }

    public StudentNotFoundException(final Throwable cause) {
        super(cause);
    }
}