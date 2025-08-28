package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SaleType {

    @Id
    @Column(name = "type_id")
    private Short id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "type_big_id")
    private SaleTypeBig saleTypeBig;

    @Column(nullable = false, length = 50)
    private String name;
}
