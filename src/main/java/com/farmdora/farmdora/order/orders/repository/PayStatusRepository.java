package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayStatusRepository extends JpaRepository<PayStatus, Short> {
    Optional<PayStatus> findByName(String name);
}
