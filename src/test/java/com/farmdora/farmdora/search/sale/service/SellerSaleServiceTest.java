package com.farmdora.farmdora.search.sale.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import com.farmdora.farmdora.sale.mapper.SaleMapper;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellerSaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SellerSaleService saleService;

    @Test
    @DisplayName("판매자의 상품 목록 검색 및 조회 서비스 레이어 테스트")
    void testSearchSales() {
        // given
        List<SaleDto> saleDtos = List.of(
                SaleDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .build(),
                SaleDto.builder()
                        .saleId(2)
                        .title("상추2")
                        .price(20000)
                        .isBlind(false)
                        .stock(200)
                        .build()
        );
        Page<SaleDto> saleSearchResponsePages = new PageImpl<>(saleDtos);
        when(saleRepository.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(saleSearchResponsePages);

        List<SaleOrderCountDto> orderCounts = List.of(
                SaleOrderCountDto.builder()
                        .saleId(1)
                        .orderCount(1L)
                        .build(),
                SaleOrderCountDto.builder()
                        .saleId(2)
                        .orderCount(1L)
                        .build()
        );
        when(saleRepository.searchSaleOrderCount(anyList())).thenReturn(orderCounts);

        List<SaleSearchResponseDto> saleSearchResponseDtos = List.of(
                SaleSearchResponseDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .orderCount(1L)
                        .build(),
                SaleSearchResponseDto.builder()
                        .saleId(2)
                        .title("상추2")
                        .price(20000)
                        .isBlind(false)
                        .stock(200)
                        .orderCount(2L)
                        .build()
        );
        when(saleMapper.mapToSaleSearchResponseDto(anyList(), anyList(), anyList())).thenReturn(saleSearchResponseDtos);

        // when
        Integer sellerId = 1;
        SaleSearchRequestDto searchCondition = SaleSearchRequestDto.builder()
                .keyword("상추")
                .sort(Sort.LATEST)
                .filters(Set.of(SaleStatus.INSTOCK))
                .typeId((short) 1)
                .typeBigId((short) 1)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSales(sellerId, searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }
}