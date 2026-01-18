package com.abhishek.bpa.service.impl;

import com.abhishek.bpa.constant.SuccessMessageConstant;
import com.abhishek.bpa.dto.auth.*;
import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.dto.common.ResponseStatus;
import com.abhishek.bpa.entity.AppUser;
import com.abhishek.bpa.entity.Organization;
import com.abhishek.bpa.enums.Role;
import com.abhishek.bpa.enums.Status;
import com.abhishek.bpa.exception.DuplicateDataException;
import com.abhishek.bpa.exception.InactiveUserException;
import com.abhishek.bpa.exception.InvalidCredentialsException;
import com.abhishek.bpa.exception.InvalidWorkspaceException;
import com.abhishek.bpa.repository.AppUserRepository;
import com.abhishek.bpa.repository.OrganizationRepository;
import com.abhishek.bpa.security.jwt.JwtService;
import com.abhishek.bpa.service.AuthService;
import com.abhishek.bpa.util.OrganizationCodeGenerator;
import com.abhishek.bpa.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<ApiResponse> signUp(SignUpRequestDto request) {

        String email = request.getEmail().toLowerCase().trim();
        String orgName = request.getOrganizationName().trim();
        String orgCode = generateUniqueOrgCode(request.getOrganizationName());

        if (appUserRepository.existsByEmailAndOrganizationName(email, orgName)){
            throw new DuplicateDataException();
        }

        try{
            Organization organization = new Organization();

            organization.setName(request.getOrganizationName().trim());
            organization.setStatus(Status.ACTIVE);
            organization.setCode(orgCode);

            Organization savedOrganization = organizationRepository.save(organization);

            AppUser appUser = new AppUser();

            appUser.setName(request.getName().trim());
            appUser.setEmail(email);
            appUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            appUser.setRole(Role.ORG_ADMIN);
            appUser.setCountryCode(request.getCountryCode());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setStatus(Status.ACTIVE);
            appUser.setOrganizationId(savedOrganization.getId());

            AppUser savedAppUser = appUserRepository.save(appUser);

            SignUpResponseDto data = new SignUpResponseDto(
                    savedOrganization.getName(),
                    savedOrganization.getCode(),
                    savedAppUser.getStatus()
            );

            ResponseStatus status = ResponseHelper.success(HttpStatus.CREATED, SuccessMessageConstant.SIGNUP);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .responseStatus(status)
                            .data(data)
                            .build()
                    );

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException();
        }

    }

    @Override
    public ResponseEntity<ApiResponse> login(LoginRequest request) {

        Organization organization = organizationRepository
                .findByCode(request.getOrganizationCode())
                .orElseThrow(InvalidWorkspaceException::new);

        AppUser user = appUserRepository
                .findByEmailAndOrganizationId(request.getEmail().toLowerCase().trim(), organization.getId())
                .orElseThrow(InvalidCredentialsException::new);

        if (user.getStatus() != Status.ACTIVE){
            throw new InactiveUserException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getOrganizationId(),
                user.getEmail(),
                user.getRole().name()
        );

        LoginResponse data = new LoginResponse(token);

        ResponseStatus status = ResponseHelper.success(HttpStatus.OK, SuccessMessageConstant.LOGIN);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseStatus(status)
                        .data(data)
                        .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAllWorkSpacesByUserEmail(WorkspaceRequestDto request) {

        String email = request.getEmail().toLowerCase().trim();

        List<WorkSpaceView> workSpaces = appUserRepository.findWorkSpacesByEmail(email);

        List<WorkSpaceResponseDto> data = workSpaces.stream()
                .map(workspace -> new WorkSpaceResponseDto(workspace.name(), workspace.code()))
                .toList();

        String message = data.isEmpty()
                ? SuccessMessageConstant.WORKSPACE_NOT_FOUND
                : SuccessMessageConstant.WORKSPACES_FOUND;

        ResponseStatus status = ResponseHelper.success(HttpStatus.OK, message);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseStatus(status)
                        .data(data)
                        .build()
        ) ;
    }

    private String generateUniqueOrgCode(String orgName){
        String base = OrganizationCodeGenerator.toSlug(orgName);
        String code = base;
        int counter = 1;

        while (organizationRepository.existsByCode(code)){
            code = base + "-" + counter;
            counter++;
        }

        return code;
    }
}
