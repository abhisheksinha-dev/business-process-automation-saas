package com.abhishek.bpa.repository;

import com.abhishek.bpa.dto.auth.WorkSpaceView;
import com.abhishek.bpa.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmailAndOrganizationId(String email, UUID organizationId);

    @Query("""
        SELECT COUNT(u) > 0
        FROM AppUser u
        JOIN Organization o ON u.organizationId = o.id
        WHERE u.email = :email
        AND LOWER(o.name) = LOWER(:orgName)
    """)
    boolean existsByEmailAndOrganizationName(
            @Param("email") String email,
            @Param("orgName") String orgName
    );

    @Query(
            """
            SELECT new com.abhishek.bpa.dto.auth.WorkSpaceView(o.name, o.code)
            FROM AppUser u
            JOIN Organization o ON u.organizationId = o.id
            WHERE LOWER(u.email) = LOWER(:email)
    """
    )
    List<WorkSpaceView> findWorkSpacesByEmail(@Param("email") String email);
}
