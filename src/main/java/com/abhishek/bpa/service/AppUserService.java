package com.abhishek.bpa.service;

import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    ResponseEntity<ApiResponse> getAllUsers();
}
