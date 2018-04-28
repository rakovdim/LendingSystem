package com.drakov.lending.controller.console;

import com.drakov.lending.exceptions.UserException;

/**
 * Created by rakov on 28.04.2018.
 */
public class ResponseEntity<T> {
    public static enum STATUS {
        OK, USER_EXCEPTION, INTERNAL_ERROR
    }

    private STATUS status;
    private T entity;
    private Exception exception;

    private ResponseEntity(STATUS status, T entity, Exception exception) {
        this.status = status;
        this.entity = entity;
        this.exception = exception;
    }

    public STATUS getStatus() {
        return status;
    }

    public T getEntity() {
        return entity;
    }

    public Exception getException() {
        return exception;
    }

    public static <T> ResponseEntity<T> ok(T entity) {
        return new ResponseEntity<T>(STATUS.OK, entity, null);
    }

    public static <T> ResponseEntity<T> userException(UserException e) {
        return new ResponseEntity<T>(STATUS.USER_EXCEPTION, null, e);
    }

    public static <T> ResponseEntity<T> internalError(Exception e) {
        return new ResponseEntity<T>(STATUS.INTERNAL_ERROR, null, e);
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "status=" + status +
                ", entity=" + entity +
                ", exception=" + exception +
                '}';
    }
}
