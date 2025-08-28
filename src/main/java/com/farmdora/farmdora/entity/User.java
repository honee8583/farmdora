package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String id;

    private String pwd;

    @Column(length = 40)
    private String name;

    @Column(length = 40)
    private String email;

    @Column(length = 50)
    private String accountNum;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "postNum", column = @Column(nullable = true)),
            @AttributeOverride(name = "addr", column = @Column(nullable = true)),
            @AttributeOverride(name = "detailAddr", column = @Column(nullable = true))
    })
    private Address address;

    private LocalDate birth;

    @Enumerated(EnumType.ORDINAL)
    private Gender sex;

    @Column(length = 30)
    private String phoneNum;

    private boolean isExpire;

    private boolean isBlind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private Auth auth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankType bankType;
}
