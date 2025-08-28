package com.farmdora.farmdora.product.category.repository;

import com.farmdora.farmdora.entity.SaleType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleTypeRepository extends JpaRepository<SaleType, Short> {

    @EntityGraph(attributePaths = {"saleTypeBig"})
    List<SaleType> findAll();
}
