package com.farmdora.farmdora.product.popup.repository;

import com.farmdora.farmdorabuyer.entity.Popup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Integer> {
    List<Popup> findByEndDateGreaterThanEqual(LocalDateTime now);

    @Query("SELECT p FROM Popup p WHERE p.endDate > :now AND p.type.name = :typeName")
    List<Popup> findValidPopups(@Param("now") LocalDateTime now, @Param("typeName") String typeName);
}
