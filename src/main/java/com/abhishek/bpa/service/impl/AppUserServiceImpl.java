package com.abhishek.bpa.service.impl;

import com.abhishek.bpa.constant.SuccessMessageConstant;
import com.abhishek.bpa.dto.appUsers.UserResponseDto;
import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.dto.common.ResponseStatus;
import com.abhishek.bpa.entity.AppUser;
import com.abhishek.bpa.mapper.AppUserMapper;
import com.abhishek.bpa.repository.AppUserRepository;
import com.abhishek.bpa.service.AppUserService;
import com.abhishek.bpa.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllUsers() {

        List<AppUser> appUsers = appUserRepository.findAll();

        List<UserResponseDto> data =  appUsers
                .stream()
                .map(appUser -> AppUserMapper.toResponse(appUser))
                .toList();

        ResponseStatus status = ResponseHelper.success(HttpStatus.OK, SuccessMessageConstant.USERS_FETCHED);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseStatus(status)
                        .data(data)
                        .build()
        );
    }
}
