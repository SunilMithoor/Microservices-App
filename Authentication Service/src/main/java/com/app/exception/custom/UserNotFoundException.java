package com.app.exception.custom;


import static com.app.utils.MessageConstants.USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}