package com.farmdora.farmdora.product.like.repository;

import com.farmdora.farmdorabuyer.entity.Like;
import com.farmdora.farmdorabuyer.entity.Sale;
import com.farmdora.farmdorabuyer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndSale(User user, Sale sale);
}
