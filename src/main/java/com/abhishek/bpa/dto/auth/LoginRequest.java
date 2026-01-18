package com.abhishek.bpa.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be in proper format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Organization code is required")
    private String organizationCode;
}
