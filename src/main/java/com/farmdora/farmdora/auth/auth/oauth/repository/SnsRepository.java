package com.farmdora.farmdora.auth.auth.oauth.repository;

import com.farmdora.farmdoraauth.entity.Sns;
import com.farmdora.farmdoraauth.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SnsRepository extends JpaRepository<Sns, Integer> {

    @Query("SELECT s.user FROM Sns s WHERE s.snsName = :snsName")
    Optional<User> findUserBySnsName(@Param("snsName") String snsName);

    List<Sns> findByUser(User user);
}
