package com.abhishek.bpa.exception;

import com.abhishek.bpa.constant.ExceptionMessageConstant;

public class DuplicateDataException extends BusinessException{

    public DuplicateDataException() {
        super(ExceptionMessageConstant.SIGNUP_FAILED_DUPLICATE_DATA, 409);
    }
}
