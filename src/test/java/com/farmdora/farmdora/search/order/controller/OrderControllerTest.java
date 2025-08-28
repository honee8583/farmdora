package com.farmdora.farmdora.search.order.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_ORDER_SUCCESS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends ControllerTest {

    @Test
    @DisplayName("판매자 주문목록 조회 API 테스트")
    void testSearchOrdersApi() throws Exception {
        // given
        PageResponseDto<OrderSearchResponseDto> pages = new PageResponseDto<>();
        List<OrderSearchResponseDto> orders = List.of(
                OrderSearchResponseDto.builder()
                        .orderId(1)
                        .products(new ArrayList<>())
                        .createdDate(LocalDateTime.now())
                        .buyerName("이다훈")
                        .totalPrice(100000)
                        .orderStatus("배송중")
                        .build(),
                OrderSearchResponseDto.builder()
                        .orderId(2)
                        .products(new ArrayList<>())
                        .createdDate(LocalDateTime.now())
                        .buyerName("이다훈")
                        .totalPrice(5000)
                        .orderStatus("판매중")
                        .build()
        );
        pages.setContents(orders);
        when(orderService.searchOrders(anyInt(), any(OrderSearchRequestDto.class), any(Pageable.class)))
                .thenReturn(pages);

        // when
        // then
        mvc.perform(get("/api/search/my/seller/order")
                        .param("userId", "1")
                        .param("searchType", "PRODUCT")
                        .param("keyword", "상품")
                        .param("searchPeriod", "TODAY")
                        .param("statusIds", "1")
                        .param("statusIds", "2")
                        .param("orderType", "RECENT")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(SEARCH_ORDER_SUCCESS.getMessage())));
    }
}