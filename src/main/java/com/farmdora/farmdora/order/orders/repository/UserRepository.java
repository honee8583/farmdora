package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u join fetch u.bankType where u.userId = :userId")
    Optional<User> findByUserIdWithBankType(@Param("userId") Integer userId);
}
