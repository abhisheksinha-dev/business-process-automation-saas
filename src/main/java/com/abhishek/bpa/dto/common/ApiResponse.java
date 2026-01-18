package com.abhishek.bpa.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private ResponseStatus responseStatus;
    private Object data;
}
