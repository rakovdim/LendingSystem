package com.drakov.lending.controller.console;

import com.drakov.lending.exceptions.UserException;

/**
 * Created by rakov on 28.04.2018.
 */
@FunctionalInterface
public interface ProcessingAction<T, R> {
    public R apply(T args) throws UserException;
}
