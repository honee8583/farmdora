package com.farmdora.farmdora.user.depot.repository;

import com.farmdora.farmdoraauth.entity.Depot;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DepotRepository extends JpaRepository<Depot, Integer> {

    List<Depot> findByUser_UserId(int userId);

    @Modifying
    @Query("update Depot d set d.isDefault = false where d.user.userId = :userId and d.isDefault = true ")
    void updateIsDefaultToFalse(@Param("userId") int userId);
}
