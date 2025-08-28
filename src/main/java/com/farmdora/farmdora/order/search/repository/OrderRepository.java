package com.farmdora.farmdora.order.search.repository;

import com.farmdora.farmdora.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer>, CustomOrderRepository {
}
