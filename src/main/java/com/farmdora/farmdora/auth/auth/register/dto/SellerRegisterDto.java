package com.farmdora.farmdora.auth.auth.register.dto;

import com.farmdora.farmdoraauth.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterDto {

    private String name;

    private String companyNum;

    private String phoneNum;

    private short authId;

    private Address address;

}
