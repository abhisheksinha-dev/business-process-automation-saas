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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

        log.info("Signup initiated for email={} organization={}", email, orgName);

        if (appUserRepository.existsByEmailAndOrganizationName(email, orgName)){
            log.warn("Signup failed: email {} already exists in organization {}", email, orgName);
            throw new DuplicateDataException();
        }

        try{
            String orgCode = generateUniqueOrgCode(request.getOrganizationName());
            log.info("Generated organization code {} for {}", orgCode, orgName);

            Organization organization = new Organization();

            organization.setName(request.getOrganizationName().trim());
            organization.setStatus(Status.ACTIVE);
            organization.setCode(orgCode);

            Organization savedOrganization = organizationRepository.save(organization);
            log.info("Organization created with id={}", savedOrganization.getId());

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
            log.info("Admin user created with id={} for orgId={}", savedAppUser.getId(), savedOrganization.getId());

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
            log.warn("Signup failed due to DB constraint for email={} org={}", email, orgName, e);
            throw new DuplicateDataException();
        }

    }

    @Override
    public ResponseEntity<ApiResponse> login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();
        String orgCode = request.getOrganizationCode();

        log.info("Login attempt for email={} workspace={}", email, orgCode);

        Organization organization = organizationRepository
                .findByCode(orgCode)
                .orElseThrow(() -> {
                    log.warn("Login failed: invalid workspace code {}", orgCode);
                    return new InvalidWorkspaceException();
                });

        AppUser user = appUserRepository
                .findByEmailAndOrganizationId(email, organization.getId())
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found for email={} orgId={}", email, organization.getId());
                    return new InvalidCredentialsException();
                });

        if (user.getStatus() != Status.ACTIVE){
            log.warn("Login blocked: inactive user id={} email={}", user.getId(), email);
            throw new InactiveUserException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            log.warn("Login failed: invalid password for userId={}", user.getId());
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getOrganizationId(),
                user.getEmail(),
                user.getRole().name()
        );

        log.info("Login successful: userId={} orgId={}", user.getId(), user.getOrganizationId());

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
        log.info("Fetching workspaces for email={}", email);

        if (!appUserRepository.existsByEmail(email)) {
            log.warn("Workspace fetch failed: user not found for email={}", email);
            throw new InvalidCredentialsException();
        }

        List<WorkSpaceView> workSpaces = appUserRepository.findWorkSpacesByEmail(email);

        if (workSpaces.isEmpty()) {
            log.warn("No workspaces found for email={}", email);
        } else {
            log.info("Found {} workspaces for email={}", workSpaces.size(), email);
        }

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
            log.debug("Organization code {} already exists, trying next", code);
            code = base + "-" + counter;
            counter++;
        }

        return code;
    }
}
