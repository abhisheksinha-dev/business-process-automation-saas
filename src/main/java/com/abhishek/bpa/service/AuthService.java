package com.abhishek.bpa.service;

import com.abhishek.bpa.dto.auth.LoginRequest;
import com.abhishek.bpa.dto.auth.LoginResponse;
import com.abhishek.bpa.dto.auth.SignUpRequestDto;
import com.abhishek.bpa.dto.auth.SignUpResponseDto;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto request);

    LoginResponse login(LoginRequest request);
}
