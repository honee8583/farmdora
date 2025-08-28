package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}

