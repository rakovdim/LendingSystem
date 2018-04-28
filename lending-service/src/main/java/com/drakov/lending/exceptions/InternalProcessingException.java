package com.drakov.lending.exceptions;

public class InternalProcessingException extends RuntimeException {

    public InternalProcessingException() {
    }

    public InternalProcessingException(String message) {
        super(message);
    }

    public InternalProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalProcessingException(Throwable cause) {
        super(cause);
    }
}
