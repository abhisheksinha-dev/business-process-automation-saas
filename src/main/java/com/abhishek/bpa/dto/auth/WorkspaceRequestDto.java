package com.abhishek.bpa.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkspaceRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be in proper format")
    private String email;
}
