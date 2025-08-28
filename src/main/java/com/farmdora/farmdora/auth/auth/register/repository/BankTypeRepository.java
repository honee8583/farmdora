package com.farmdora.farmdora.auth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.BankType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankTypeRepository extends JpaRepository<BankType, Short> {

    Optional<BankType> findById(Short id);

}
