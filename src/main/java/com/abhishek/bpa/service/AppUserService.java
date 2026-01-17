package com.abhishek.bpa.service;

import com.abhishek.bpa.dto.appUsers.UserResponseDto;

import java.util.List;

public interface AppUserService {

    List<UserResponseDto> getAllUsers();
}
