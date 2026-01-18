package com.abhishek.bpa.controller;

import com.abhishek.bpa.constant.EndPointConstant;
import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndPointConstant.USER)
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @GetMapping(EndPointConstant.GET_ALL)
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}
