package com.abhishek.bpa.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticatedUser {

    private UUID userId;
    private UUID organizationId;
    private String email;
    private String role;
}
