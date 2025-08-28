package com.farmdora.farmdora.product.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleFileDto {
    private String saveFile;
    private String originFile;
    private boolean isMain;
}