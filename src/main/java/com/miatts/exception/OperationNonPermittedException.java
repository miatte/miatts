package com.miatts.exception;

public class OperationNonPermittedException extends RuntimeException {

    public OperationNonPermittedException(String message) {
        super(message);
    }
}
