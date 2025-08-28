package com.farmdora.farmdora.order.search.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import com.farmdora.farmdora.order.mapper.OrderMapper;
import com.farmdora.farmdora.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public PageResponseDto<OrderSearchResponseDto> searchOrders(Integer userId, OrderSearchRequestDto searchCondition, Pageable pageable) {
        Page<OrderDto> orders = orderRepository.searchOrders(userId, searchCondition, pageable);
        List<Integer> orderIds = orders.stream().map(OrderDto::getOrderId).toList();

        List<OrderDetailDto> orderDetails = null;
        if (!orderIds.isEmpty()) {
            orderDetails = orderRepository.findOrderDetailsByIds(orderIds, searchCondition.getSort());
            return orderMapper.mapToOrderSearchResponseDto(orders, orderDetails);
        }

        return orderMapper.mapToOrderSearchResponseDto(orders, null);
    }
}
