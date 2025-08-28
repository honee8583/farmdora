package com.farmdora.farmdora.product.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastSearchDto {
    private String keyword;
    private String sort;
    private int page;
    private int size;
}
