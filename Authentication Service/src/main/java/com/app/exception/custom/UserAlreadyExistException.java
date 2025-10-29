package com.app.exception.custom;


import static com.app.utils.MessageConstants.USERS_ALREADY_EXIST;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super(USERS_ALREADY_EXIST);
    }
}