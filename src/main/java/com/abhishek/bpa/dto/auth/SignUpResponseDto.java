package com.abhishek.bpa.dto.auth;

import com.abhishek.bpa.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResponseDto {

    private String organizationName;
    private String organizationCode;
    private Status userStatus;
}
