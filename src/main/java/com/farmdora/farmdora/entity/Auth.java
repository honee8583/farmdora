package com.farmdora.farmdora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auth {

    @Id
    @Column(name = "auth_id")
    private Short id;

    @Column(nullable = false, length = 50)
    private String role;
}
