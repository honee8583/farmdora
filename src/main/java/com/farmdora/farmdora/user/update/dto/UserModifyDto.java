package com.farmdora.farmdora.user.update.dto;

import com.farmdora.farmdoraauth.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyDto {
    private String pwd;
    private String phoneNum;
    private String email;
    private String accountNum;
    private short bankId;
    private Address address;
}
