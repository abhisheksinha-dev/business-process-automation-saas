package com.abhishek.bpa.exception;

import com.abhishek.bpa.constant.ExceptionMessageConstant;

public class InvalidWorkspaceException extends BusinessException{

    public InvalidWorkspaceException() {
        super(ExceptionMessageConstant.WORKSPACE_NOT_FOUND, 404);
    }
}
