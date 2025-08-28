package com.farmdora.farmdora.product.basket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponseDto {
    private Integer basketId;
    private Integer saleId;
    private String title;
    private String option;
    private Integer stock;
    private Integer quantity;
    private Integer price;
    private String imageUrl;
}
