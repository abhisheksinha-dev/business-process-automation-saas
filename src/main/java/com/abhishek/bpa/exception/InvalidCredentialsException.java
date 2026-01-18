package com.abhishek.bpa.exception;

import com.abhishek.bpa.constant.ExceptionMessageConstant;

public class InvalidCredentialsException extends BusinessException{

    public InvalidCredentialsException() {
        super(ExceptionMessageConstant.INVALID_CREDENTIALS, 401);
    }
}
