package com.abhishek.bpa.service.impl;

import com.abhishek.bpa.constant.MessageConstant;
import com.abhishek.bpa.dto.auth.LoginRequest;
import com.abhishek.bpa.dto.auth.LoginResponse;
import com.abhishek.bpa.dto.auth.SignUpRequestDto;
import com.abhishek.bpa.dto.auth.SignUpResponseDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (appUserRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException(MessageConstant.EMAIL_ALREADY_REGISTERED);
        }

        Organization organization = new Organization();

        organization.setName(request.getOrganizationName());
        organization.setStatus(Status.ACTIVE);
        organization.setCode(OrganizationCodeGenerator.generate(request.getOrganizationName()));

        Organization savedOrganization = organizationRepository.save(organization);

        AppUser appUser = new AppUser();

        appUser.setName(request.getName());
        appUser.setEmail(request.getEmail());
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
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        AppUser user = appUserRepository.findByEmail(request.getEmail())
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
}
