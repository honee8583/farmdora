package com.farmdora.farmdora.order.search.repository;

import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomOrderRepository {
    Page<OrderDto> searchOrders(Integer sellerId, OrderSearchRequestDto searchCondition, Pageable pageable);
    List<OrderDetailDto> findOrderDetailsByIds(List<Integer> ids, Sort sort);
}
