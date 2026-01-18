package com.abhishek.bpa.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class WorkSpaceResponseDto {

    private String organizationName;
    private String organizationCode;
}
