package com.farmdora.farmdora.auth.auth.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnsRegisterDto {

    private int userId;
    private String snsName;
    private short typeId;

}
