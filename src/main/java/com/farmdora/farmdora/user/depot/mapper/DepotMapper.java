package com.farmdora.farmdora.user.depot.mapper;

import com.farmdora.farmdoraauth.entity.Depot;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotModifyRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotRegisterRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotSelectResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DepotMapper {

    //추가
    public Depot toEntity(DepotRegisterRequestDto dto, User user) {
        return  Depot.builder()
                .user(user) // 조회된 User 객체를 매핑
                .deliveryName(dto.getDeliveryName())
                .name(dto.getReceiverName())
                .phoneNum(dto.getPhoneNum())
                .address(dto.getAddress())
                .require(dto.getRequire())
                .isDefault(dto.isDefaultAddr())
                .build();

    }

    //조회
    public DepotSelectResponseDto toDto(Depot depot) {
        return DepotSelectResponseDto.builder()
                .depotId(depot.getId())
                .deliveryName(depot.getDeliveryName())
                .receiverName(depot.getName())
                .phoneNum(depot.getPhoneNum())
                .address(depot.getAddress())
                .require(depot.getRequire())
                .defaultAddr(depot.isDefault())
                .build();
    }

    //수정
    public void updateEntityFormDto(DepotModifyRequestDto dto, Depot depot){
        depot.updateInfo(
                dto.getDeliveryName(),
                dto.getReceiverName(),
                dto.getPhoneNum(),
                dto.getAddress(),
                dto.getRequire(),
                dto.isDefaultAddr()
        );
    }
}
