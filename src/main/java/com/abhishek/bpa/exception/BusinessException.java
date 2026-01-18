package com.abhishek.bpa.exception;

/**
 * All business errors extend this
 * HTTP status stored here
 * Controller doesn’t decide status
 */
public abstract class BusinessException extends RuntimeException{

    private final int httpStatus;

    protected BusinessException(String message, int httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus(){
        return httpStatus;
    }
}
