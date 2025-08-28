package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depot_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(length = 50)
    private String deliveryName;

    @Column(length = 50)
    private String name;

    @Column(length = 30)
    private String phoneNum;

    @Embedded
    private Address address;

    private String require;

    private boolean isDefault;
}
