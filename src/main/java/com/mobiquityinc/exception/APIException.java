package com.mobiquityinc.exception;

public class APIException extends RuntimeException {

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Exception e) {
        super(message, e);
    }
}
