package com.farmdora.farmdora.order.search.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class OrderDetailDto {
    private Integer orderId;
    private Integer saleId;
    private String saleTitle;
    private Integer optionId;
    private String optionName;
    private Integer quantity;
    private Integer price;

    @QueryProjection
    public OrderDetailDto(Integer orderId,
                          Integer saleId,
                          String saleTitle,
                          Integer optionId,
                          String optionName,
                          Integer quantity,
                          Integer price) {
        this.orderId = orderId;
        this.saleId = saleId;
        this.saleTitle = saleTitle;
        this.optionId = optionId;
        this.optionName = optionName;
        this.quantity = quantity;
        this.price = price;
    }
}
