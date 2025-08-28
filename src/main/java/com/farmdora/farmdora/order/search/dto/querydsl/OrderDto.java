package com.farmdora.farmdora.order.search.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private String buyerName;
    private String orderStatus;
    private Integer totalPrice;
    private LocalDateTime createdDate;

    @QueryProjection
    public OrderDto(Integer orderId,
                    String buyerName,
                    String orderStatus,
                    Integer totalPrice,
                    LocalDateTime createdDate) {
        this.orderId = orderId;
        this.buyerName = buyerName;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.createdDate = createdDate;
    }
}
