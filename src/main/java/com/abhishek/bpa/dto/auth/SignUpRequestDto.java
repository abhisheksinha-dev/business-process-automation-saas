package com.abhishek.bpa.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {

    private String organizationName;
    private String name;
    private String email;
    private String password;
    private String countryCode;
    private String phoneNumber;
}
