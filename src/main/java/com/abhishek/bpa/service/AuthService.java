package com.abhishek.bpa.service;

import com.abhishek.bpa.dto.auth.*;

import java.util.List;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto request);

    LoginResponse login(LoginRequest request);

    List<WorkSpaceResponseDto> getAllWorkSpacesByUserEmail(WorkspaceRequestDto request);
}
