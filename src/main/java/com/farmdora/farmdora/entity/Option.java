package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`option`")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column(length = 50)
    private String name;

    private int price;

    private int quantity;

    private boolean isStop;

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
