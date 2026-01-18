package com.abhishek.bpa.service.impl;

import com.abhishek.bpa.constant.MessageConstant;
import com.abhishek.bpa.dto.auth.*;
import com.abhishek.bpa.entity.AppUser;
import com.abhishek.bpa.entity.Organization;
import com.abhishek.bpa.enums.Role;
import com.abhishek.bpa.enums.Status;
import com.abhishek.bpa.repository.AppUserRepository;
import com.abhishek.bpa.repository.OrganizationRepository;
import com.abhishek.bpa.security.jwt.JwtService;
import com.abhishek.bpa.service.AuthService;
import com.abhishek.bpa.util.OrganizationCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto request) {

        try{
            Organization organization = new Organization();

            organization.setName(request.getOrganizationName());
            organization.setStatus(Status.ACTIVE);
            organization.setCode(OrganizationCodeGenerator.generate(request.getOrganizationName()));

            Organization savedOrganization = organizationRepository.save(organization);

            AppUser appUser = new AppUser();

            appUser.setName(request.getName());
            appUser.setEmail(request.getEmail().toLowerCase().trim());
            appUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            appUser.setRole(Role.ORG_ADMIN);
            appUser.setCountryCode(request.getCountryCode());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setStatus(Status.ACTIVE);
            appUser.setOrganizationId(savedOrganization.getId());

            AppUser savedAppUser = appUserRepository.save(appUser);

            return new SignUpResponseDto(
                    savedOrganization.getName(),
                    savedOrganization.getCode(),
                    savedAppUser.getStatus()
            );
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(MessageConstant.SIGNUP_FAILED_DUPLICATE_DATA);
        }

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Organization organization = organizationRepository
                .findByCode(request.getOrganizationCode())
                .orElseThrow(() -> new RuntimeException(MessageConstant.INVALID_WORKSPACE));

        AppUser user = appUserRepository
                .findByEmailAndOrganizationId(request.getEmail().toLowerCase().trim(), organization.getId())
                .orElseThrow(() -> new RuntimeException(MessageConstant.INVALID_CREDENTIALS));

        if (user.getStatus() != Status.ACTIVE){
            throw new RuntimeException(MessageConstant.INACTIVE_USER);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw new RuntimeException(MessageConstant.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getOrganizationId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(token);
    }

    @Override
    public List<WorkSpaceResponseDto> getAllWorkSpacesByUserEmail(WorkspaceRequestDto request) {

        List<AppUser> users = appUserRepository.findAllByEmail(request.getEmail().toLowerCase().trim());

        return users.stream()
                .map(user -> {
                    Organization org = organizationRepository.findById(user.getOrganizationId())
                            .orElseThrow(() -> new RuntimeException(MessageConstant.WORKSPACE_NOT_FOUND));

                    return new WorkSpaceResponseDto(
                            org.getName(),
                            org.getCode()
                    );
                })
                .toList();
    }
}
