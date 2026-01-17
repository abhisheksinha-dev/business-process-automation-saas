package com.abhishek.bpa.entity;

import com.abhishek.bpa.enums.Role;
import com.abhishek.bpa.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "super_admin_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformUser extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
