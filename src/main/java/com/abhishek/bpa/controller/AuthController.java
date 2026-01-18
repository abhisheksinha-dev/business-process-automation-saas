package com.abhishek.bpa.controller;

import com.abhishek.bpa.constant.EndPointConstant;
import com.abhishek.bpa.dto.auth.*;
import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(EndPointConstant.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(EndPointConstant.SIGN_UP)
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequestDto request){
        return authService.signUp(request);
    }

    @PostMapping(EndPointConstant.LOGIN)
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping(EndPointConstant.WORKSPACE)
    public ResponseEntity<ApiResponse> getAllWorkSpacesByUserEmail(@Valid @RequestBody WorkspaceRequestDto request){
        return authService.getAllWorkSpacesByUserEmail(request);
    }
}
