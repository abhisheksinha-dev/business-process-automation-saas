package com.abhishek.bpa.initializer;


import com.abhishek.bpa.entity.PlatformUser;
import com.abhishek.bpa.enums.Role;
import com.abhishek.bpa.enums.Status;
import com.abhishek.bpa.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements CommandLineRunner {

    private final PlatformUserRepository platformUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${platform.superadmin.email}")
    private String email;

    @Value("${platform.superadmin.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {

        if (platformUserRepository.existsByRole(Role.SUPER_ADMIN)){
            log.info("Super Admin already exists with email : {}", email);
            return;
        }

        PlatformUser superAdminUser = PlatformUser
                .builder()
                .name("ABHISHEK SINHA")
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.SUPER_ADMIN)
                .status(Status.ACTIVE)
                .build();

        PlatformUser savedSuperAdmin = platformUserRepository.save(superAdminUser);
        log.info("Super Admin Created Successfully with email: {}", savedSuperAdmin.getEmail());
    }
}
