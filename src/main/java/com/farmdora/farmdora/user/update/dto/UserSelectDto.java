package com.farmdora.farmdora.user.update.dto;

import com.farmdora.farmdoraauth.entity.Address;
import com.farmdora.farmdoraauth.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSelectDto {
    private String name;
    private String id;
    private String pwd;
    private String phoneNum;
    private String email;
    private String accountNum;
    private LocalDate birth;
    private Gender sex;
    private BankTypeDto bankType;
    private Address address;
}
