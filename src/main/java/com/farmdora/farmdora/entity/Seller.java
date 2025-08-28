package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "company_num", unique = true)
    private String companyNum;

    @Column(nullable = false, length = 50)
    private String name;

    @Embedded
    private Address address;

    @Column(nullable = false, length = 30)
    private String phoneNum;

    @Column(nullable = false)
    private String saveFile;

    @Column(nullable = false)
    private String originFile;

    @Column(nullable = false)
    private boolean isApprove;
}
