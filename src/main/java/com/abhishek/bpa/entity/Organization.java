package com.abhishek.bpa.entity;
import com.abhishek.bpa.enums.Status;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
}
