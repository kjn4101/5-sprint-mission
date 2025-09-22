package com.codeit.blog.exception.user;

import com.codeit.blog.exception.BaseException;
import com.codeit.blog.exception.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
    public UserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 