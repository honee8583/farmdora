package com.farmdora.farmdora.user.update.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTypeDto {
    private short bankId;
    private String bankName;
}
