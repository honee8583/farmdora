package com.farmdora.farmdora.product.sale.repository;

import com.farmdora.farmdoraproduct.entity.Sale;
import com.farmdora.farmdoraproduct.entity.SaleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleFileRepository extends JpaRepository<SaleFile, Integer> {
    List<SaleFile> findBySale(Sale sale);
    Optional<SaleFile> findFirstBySaleIdAndIsMainFalse(Integer saleId);
}