package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Short> {
    Optional<OrderStatus> findByName(String name);
}
