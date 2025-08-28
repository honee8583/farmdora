package com.farmdora.farmdora.order.search.mapper;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto.OptionResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto.ProductResponseDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public PageResponseDto<OrderSearchResponseDto> mapToOrderSearchResponseDto(Page<OrderDto> orders, List<OrderDetailDto> orderDetails) {
        List<OrderSearchResponseDto> orderSearchResponseDtos = toOrderList(orders);
        Map<Integer, OrderSearchResponseDto> orderMap = toOrderMap(orderSearchResponseDtos);

        if (orderDetails != null) {
            addOrderDetails(orderMap, orderDetails);
        }
        return new PageResponseDto<>(orderSearchResponseDtos, orders);
    }

    private List<OrderSearchResponseDto> toOrderList(Page<OrderDto> orders) {
        return orders.stream()
                .map(order ->
                        OrderSearchResponseDto.builder()
                                .orderId(order.getOrderId())
                                .totalPrice(order.getTotalPrice())
                                .createdDate(order.getCreatedDate())
                                .buyerName(order.getBuyerName())
                                .orderStatus(order.getOrderStatus())
                                .products(new ArrayList<>())
                                .build()
                ).toList();
    }

    private Map<Integer, OrderSearchResponseDto> toOrderMap(List<OrderSearchResponseDto> orderSearchResponseDtos) {
        return orderSearchResponseDtos
                .stream()
                .collect(Collectors.toMap(OrderSearchResponseDto::getOrderId, o -> o));
    }

    private void addOrderDetails(Map<Integer, OrderSearchResponseDto> orderMap, List<OrderDetailDto> orderDetails) {
        for (OrderDetailDto orderDetail : orderDetails) {
            Integer orderId = orderDetail.getOrderId();
            Integer saleId = orderDetail.getSaleId();
            String saleTitle = orderDetail.getSaleTitle();
            Integer optionId = orderDetail.getOptionId();
            String optionName = orderDetail.getOptionName();
            Integer quantity = orderDetail.getQuantity();
            Integer price = orderDetail.getPrice();

            OrderSearchResponseDto order = orderMap.get(orderId);
            if (order == null) {
                continue;
            }

            ProductResponseDto product = order
                    .getProducts()
                    .stream()
                    .filter(p -> p.getSaleId().equals(saleId))
                    .findFirst()
                    .orElseGet(() -> {
                        ProductResponseDto productTmp = ProductResponseDto.builder()
                                .saleId(saleId)
                                .saleTitle(saleTitle)
                                .options(new ArrayList<>())
                                .build();
                        order.getProducts().add(productTmp);
                        return productTmp;
                    });
            product
                    .getOptions()
                    .add(new OptionResponseDto(optionId, optionName, quantity, price));
        }
    }
}
