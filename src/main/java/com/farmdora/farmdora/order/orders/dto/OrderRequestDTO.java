package com.farmdora.farmdora.order.orders.dto;

import lombok.*;

import java.util.List;

public class OrderRequestDTO {

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderFromBasketDTO {
        private Integer depotId;
        private List<Integer> basketIds;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderFromOptionDTO {
        private Integer depotId;
        private Integer optionId;
        private Integer quantity;
    }
}
