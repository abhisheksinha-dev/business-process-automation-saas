package com.abhishek.bpa.controller;

import com.abhishek.bpa.constant.EndPointConstant;
import com.abhishek.bpa.dto.auth.*;
import com.abhishek.bpa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(EndPointConstant.WORKSPACE)
    public List<WorkSpaceResponseDto> getAllWorkSpacesByUserEmail(@RequestBody WorkspaceRequestDto request){
        return authService.getAllWorkSpacesByUserEmail(request);
    }
}
