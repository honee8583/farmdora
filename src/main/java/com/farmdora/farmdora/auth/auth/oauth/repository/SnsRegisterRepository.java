package com.farmdora.farmdora.auth.auth.oauth.repository;

import com.farmdora.farmdoraauth.entity.Sns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsRegisterRepository extends JpaRepository<Sns, Short> {

    boolean existsBySnsName(String snsName);
}
