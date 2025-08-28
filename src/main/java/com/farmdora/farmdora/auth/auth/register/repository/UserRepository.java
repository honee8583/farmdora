package com.farmdora.farmdora.auth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.UserAddressDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"auth"})
    Optional<User> findById(String id);

    @Query("select u.userId from User u where u.id =:id ")
    int findUserIdById(@Param("id") String id);

    @Query("SELECT u.id from User u where u.email= :email")
    String findIdByEmail(@Param("email") String email);

    @Query("update User u set u.pwd = :pwd where u.email = :email")
    @Modifying
    void updatePwdByEmail(String email, String pwd);

    boolean existsUserByEmailAndId(String email,String id);

    boolean existsUserByEmailAndName(String email, String name);

    @Query("SELECT new com.farmdora.farmdoraauth.mypage.user.depot.dto.UserAddressDto(u.name, u.phoneNum, u.address) FROM User u WHERE u.userId = :userId")
    UserAddressDto findUserAddressByUserId(@Param("userId") Integer userId);
}
