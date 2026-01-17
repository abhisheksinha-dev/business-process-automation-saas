package com.abhishek.bpa.service.impl;

import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.entity.AppUser;
import com.abhishek.bpa.mapper.AppUserMapper;
import com.abhishek.bpa.repository.AppUserRepository;
import com.abhishek.bpa.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<AppUser> appUsers = appUserRepository.findAll();
        return appUsers
                .stream()
                .map(appUser -> AppUserMapper.toResponse(appUser))
                .toList();
    }
}
