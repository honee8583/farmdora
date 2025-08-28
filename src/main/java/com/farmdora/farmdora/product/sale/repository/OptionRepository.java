package com.farmdora.farmdora.product.sale.repository;

import com.farmdora.farmdoraproduct.entity.Option;
import com.farmdora.farmdoraproduct.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    List<Option> findBySale(Sale sale);

    Optional<Option> findFirstBySaleIdOrderByIdAsc(Integer saleId);
}