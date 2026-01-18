package com.abhishek.bpa.service;

import com.abhishek.bpa.dto.auth.*;
import com.abhishek.bpa.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;


public interface AuthService {

    ResponseEntity<ApiResponse> signUp(SignUpRequestDto request);

    ResponseEntity<ApiResponse> login(LoginRequest request);

    ResponseEntity<ApiResponse> getAllWorkSpacesByUserEmail(WorkspaceRequestDto request);
}
