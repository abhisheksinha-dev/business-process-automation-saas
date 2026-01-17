package com.abhishek.bpa.repository;

import com.abhishek.bpa.entity.PlatformUser;
import com.abhishek.bpa.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlatformUserRepository extends JpaRepository<PlatformUser, UUID> {

    boolean existsByRole(Role role);
}
