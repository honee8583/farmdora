package com.farmdora.farmdora.product.sale.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// register와 update에서 같이 사용
public class SaleRequestDto {
    private Integer sellerId;
    private Integer typeId;  // SaleType의 ID
    // 업데이트에서만 사용
    private Integer saleId;
    private String title;
    private String content;
    private String origin;
    private List<SaleFileDto> files;
    private List<OptionDto> options;
}