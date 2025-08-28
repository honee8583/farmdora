package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.Order;
import com.farmdora.farmdorabuyer.entity.OrderOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOptionRepository extends JpaRepository<OrderOption, Integer> {

    // 주어진 order 목록에 포함된 모든 주문 옵션을 찾는 함수
    List<OrderOption> findAllByOrderIn(List<Order> orders);

    // 주문별 주문 옵션 목록 조회
    List<OrderOption> findByOrder(Order order);

    List<OrderOption> findByOrderId(Integer orderId);
}