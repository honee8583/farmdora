package com.farmdora.farmdora.order.search.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResponseDto {
    private Integer orderId;
    private List<ProductResponseDto> products;
    private LocalDateTime createdDate;
    private String buyerName;
    private Integer totalPrice;
    private String orderStatus;

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponseDto {
        private Integer saleId;
        private String saleTitle;
        private List<OptionResponseDto> options;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionResponseDto {
        private Integer optionId;
        private String name;
        private Integer count;
        private Integer price;
    }
}
