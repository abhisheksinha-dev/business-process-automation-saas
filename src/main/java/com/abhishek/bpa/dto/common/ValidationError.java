package com.abhishek.bpa.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String message;
}
