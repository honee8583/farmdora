package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.RefundType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundTypeRepository extends JpaRepository<RefundType, Short> {
}
