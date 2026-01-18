package com.abhishek.bpa.util;

import com.abhishek.bpa.dto.common.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ResponseHelper {

    private ResponseHelper() {}

    public static ResponseStatus success(HttpStatus status, String message){
        return ResponseStatus.builder()
                .success(true)
                .httpStatus(status.value())
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
