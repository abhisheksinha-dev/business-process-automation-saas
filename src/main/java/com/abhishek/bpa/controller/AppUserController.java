package com.abhishek.bpa.controller;

import com.abhishek.bpa.constant.EndPointConstant;
import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.service.AppUserService;
import lombok.RequiredArgsConstructor;
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
    public List<UserResponseDto> getAllUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getAuthorities().forEach(a ->
                System.out.println(a.getAuthority())
        );

        return userService.getAllUsers();
    }
}
