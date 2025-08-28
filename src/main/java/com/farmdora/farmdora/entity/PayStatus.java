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
public class PayStatus {

    @Id
    @Column(name = "status_id")
    private Short id;

    @Column(length = 50)
    private String name;
}
