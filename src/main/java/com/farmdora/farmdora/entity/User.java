package com.farmdora.farmdora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "`user`",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_login", columnNames = {"username"}),
                @UniqueConstraint(name = "uk_user_email", columnNames = {"email"})
        }
)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String username;

    private String password;

    @Column(length = 60)
    private String name;

    @Column(length = 120)
    private String email;

    @Embedded
    private Address address;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 30)
    private String phone;

    private boolean isExpire = false;

    private boolean isBlind = false;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

}
