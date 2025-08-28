package com.farmdora.farmdora.user.update.mapper;

import com.farmdora.farmdoraauth.entity.BankType;
import com.farmdora.farmdoraauth.mypage.user.update.dto.BankTypeDto;
import org.springframework.stereotype.Component;

@Component
public class BankTypeMapper {
    public BankTypeDto bankTypeToDto(BankType bankType) {
        return BankTypeDto.builder()
                .bankId(bankType.getId())
                .bankName(bankType.getName())
                .build();
    }
}
