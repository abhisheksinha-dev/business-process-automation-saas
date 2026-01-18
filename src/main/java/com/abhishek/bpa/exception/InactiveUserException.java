package com.abhishek.bpa.exception;

import com.abhishek.bpa.constant.ExceptionMessageConstant;

public class InactiveUserException extends BusinessException{

    public InactiveUserException() {
        super(ExceptionMessageConstant.INACTIVE_USER, 403);
    }
}
