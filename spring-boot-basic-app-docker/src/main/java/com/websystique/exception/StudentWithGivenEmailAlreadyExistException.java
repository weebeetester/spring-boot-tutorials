package com.websystique.exception;

public class StudentWithGivenEmailAlreadyExistException extends RuntimeException {

    public StudentWithGivenEmailAlreadyExistException() {
        super();
    }

    public StudentWithGivenEmailAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StudentWithGivenEmailAlreadyExistException(final String message) {
        super(message);
    }

    public StudentWithGivenEmailAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
