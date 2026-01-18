package com.abhishek.bpa.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Organization admin name is required")
    private String name;

    @NotBlank(message = "Organization admin email is required")
    @Email(message = "Email should be in proper format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Country code is required")
    private String countryCode;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}
