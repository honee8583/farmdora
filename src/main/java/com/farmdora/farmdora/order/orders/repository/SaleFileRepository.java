package com.farmdora.farmdora.order.orders.repository;

import com.farmdora.farmdorabuyer.entity.SaleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleFileRepository extends JpaRepository<SaleFile, Integer> {

    List<SaleFile> findBySaleIdInAndIsMainFalse(List<Integer> saleId);

    Optional<SaleFile> findBySaleIdAndIsMainTrue(Integer saleId);

    Optional<SaleFile> findBySaleIdAndIsMainFalse(Integer saleId);

}
