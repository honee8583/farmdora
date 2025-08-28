package com.farmdora.farmdora.user.depot.dto;

import com.farmdora.farmdoraauth.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepotSelectResponseDto {
    private Integer depotId;
    private String deliveryName;
    private String receiverName;
    private String phoneNum;
    private Address address;
    private String require;
    private boolean defaultAddr;
}
