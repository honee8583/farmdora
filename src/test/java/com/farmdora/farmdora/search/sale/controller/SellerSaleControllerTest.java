package com.farmdora.farmdora.search.sale.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Set;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_SALES_SUCCESS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerSaleControllerTest extends ControllerTest {

    private SaleSearchRequestDto searchCondition;
    private PageResponseDto<SaleSearchResponseDto> result;

    @BeforeEach
    void setUp() {
        searchCondition  = SaleSearchRequestDto.builder()
                .keyword("상추")
                .sort(Sort.LATEST)
                .filters(Set.of(SaleStatus.INSTOCK))
                .typeBigId((short) 1)
                .typeId((short) 2)
                .build();

        List<SaleSearchResponseDto> saleSearchResponseDtos = List.of(
                SaleSearchResponseDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .orderCount(3L)
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
        Page<SaleSearchResponseDto> pages = new PageImpl<>(saleSearchResponseDtos);
        result = new PageResponseDto(pages.getContent(), pages);
    }

    @Nested
    @DisplayName("상품 목록 검색 API 테스트")
    class SearchSales {

        @Test
        @DisplayName("JSON으로 상품 목록 조회 API 테스트")
        void testSearchSalesByJsonAPI() throws Exception {
            // given
            when(sellerSaleService.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(post("/api/search/my/seller/sale")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(searchCondition)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_SALES_SUCCESS.getMessage())));
        }

        @Test
        @DisplayName("파라미터로 상품 목록 조회 API 테스트")
        void testSearchSalesByParamsAPI() throws Exception {
            // given
            when(sellerSaleService.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(get("/api/search/my/seller/sale")
                            .param("keyword", "상추")
                            .param("sort", "LATEST")
                            .param("typeBigId", "1")
                            .param("typeId", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_SALES_SUCCESS.getMessage())));
        }
    }
}