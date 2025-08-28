package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.Order;
import com.farmdora.farmdorabuyer.entity.Review;
import com.farmdora.farmdorabuyer.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    boolean existsByOrderAndSale(
            Order order,
            Sale sale);

    Page<Review> findAllByOrderUserUserIdAndCreatedDateBetweenOrderByCreatedDateDesc(
            Integer userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);

}