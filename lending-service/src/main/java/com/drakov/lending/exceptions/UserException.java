package com.drakov.lending.exceptions;

import java.text.MessageFormat;

public class UserException extends Exception {

    public UserException() {
    }

    public UserException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }

    public UserException(String message) {
        super(message);
    }


    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
