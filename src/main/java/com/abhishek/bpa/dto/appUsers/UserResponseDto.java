package com.abhishek.bpa.dto.appUsers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String status;
    private String countryCode;
    private String phoneNumber;
}
