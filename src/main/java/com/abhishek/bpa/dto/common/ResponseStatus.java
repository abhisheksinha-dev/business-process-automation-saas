package com.abhishek.bpa.dto.common;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {

    private boolean success;
    private int httpStatus;
    private String message;
    private Instant timestamp;
}
