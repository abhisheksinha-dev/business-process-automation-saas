package com.abhishek.bpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(unique = true, length = 50)
    private String organizationDomain;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100, unique = true)
    private String email;

    @Column(name = "country_code", length = 5)
    private String countryCode;

    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String status;
}
