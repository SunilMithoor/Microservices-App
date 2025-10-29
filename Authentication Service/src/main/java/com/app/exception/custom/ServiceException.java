package com.app.exception.custom;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}