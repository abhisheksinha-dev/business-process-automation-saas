package com.abhishek.bpa.mapper;

import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.entity.AppUser;

public class AppUserMapper {

    public static UserResponseDto toResponse(AppUser appUser){

        return UserResponseDto.builder()
                .id(appUser.getId())
                .name(appUser.getName())
                .email(appUser.getEmail())
                .status(appUser.getStatus().name())
                .role(appUser.getRole().name())
                .countryCode(appUser.getCountryCode())
                .phoneNumber(appUser.getPhoneNumber())
                .build();
    }
}
