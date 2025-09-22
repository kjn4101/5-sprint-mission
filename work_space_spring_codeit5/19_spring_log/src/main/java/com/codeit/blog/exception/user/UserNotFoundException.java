package com.codeit.blog.exception.user;

import com.codeit.blog.exception.ErrorCode;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public static UserNotFoundException withUsername(String username) {
        UserNotFoundException exception = new UserNotFoundException();
        exception.addDetail("username", username);
        return exception;
    }

    public static UserNotFoundException withId(String userId) {
        UserNotFoundException exception = new UserNotFoundException();
        exception.addDetail("userId", userId);
        return exception;
    }

    public static UserNotFoundException withMessage(String message) {
        UserNotFoundException exception = new UserNotFoundException();
        exception.addDetail("message", message);
        return exception;
    }
}
