package com.farmdora.farmdora.search.sale.mapper;

import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SaleMapperTest {

    private final SaleMapper saleMapper = new SaleMapper();

    @Test
    @DisplayName("상품정보, 상품의 주문 수를 하나의 DTO로 매핑")
    void testMapToSaleSearchResponseDto() {
        // given
        List<Integer> saleIds = new ArrayList<>();
        List<SaleDto> sales = new ArrayList<>();
        List<SaleOrderCountDto> orderCounts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            saleIds.add(i);
            sales.add(new SaleDto(i, "상추" + i, false, i * 1000, i * 100));
            orderCounts.add(new SaleOrderCountDto(i, (long) i));
        }

        // when
        List<SaleSearchResponseDto> result = saleMapper.mapToSaleSearchResponseDto(saleIds, sales, orderCounts);

        // then
        SaleSearchResponseDto saleDetail = result.get(0);
        assertThat(saleDetail.getOrderCount()).isEqualTo(1);
        assertThat(saleDetail.getTitle()).isEqualTo("상추1");
        assertThat(saleDetail.getSaleId()).isEqualTo(1);
        assertThat(saleDetail.getPrice()).isEqualTo(1000);
        assertThat(saleDetail.getStock()).isEqualTo(100);
    }
}