package com.abhishek.bpa.controller;

import com.abhishek.bpa.constant.EndPointConstant;
import com.abhishek.bpa.dto.auth.LoginRequest;
import com.abhishek.bpa.dto.auth.LoginResponse;
import com.abhishek.bpa.dto.auth.SignUpRequestDto;
import com.abhishek.bpa.dto.auth.SignUpResponseDto;
import com.abhishek.bpa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPointConstant.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(EndPointConstant.SIGN_UP)
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto request){
        return authService.signUp(request);
    }

    @PostMapping(EndPointConstant.LOGIN)
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}
