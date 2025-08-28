package com.farmdora.farmdora.search.sale.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.sale.dto.*;
import com.farmdora.farmdora.sale.dto.SaleDetailDto.OptionDetailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaleControllerTest extends ControllerTest {

    @Nested
    @DisplayName("상품 상세 조회 API 테스트")
    class GetSaleDetail {

        @Test
        @DisplayName("상품 상세 조회 API 성공")
        void testGetSaleDetailAPI() throws Exception {
            // given
            SaleDetailDto saleDetailDto = SaleDetailDto.builder()
                    .files(List.of("file1", "file2"))
                    .options(List.of(new OptionDetailDto(), new OptionDetailDto()))
                    .origin("국산")
                    .isLike(true)
                    .content("내용")
                    .build();
            when(saleService.getSaleDetail(anyInt(), anyInt())).thenReturn(saleDetailDto);

            // when
            // then
            mvc.perform(get("/api/search/sale/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(GET_SALE_DETAIL_SUCCESS.getMessage())))
                    .andExpect(jsonPath("$.data.files.size()", equalTo(2)))
                    .andExpect(jsonPath("$.data.origin", equalTo("국산")));
        }

        @Test
        @DisplayName("상품 상세 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetSaleDetailAPI_SaleNotFoundException() throws Exception {
            // given
            when(saleService.getSaleDetail(anyInt(), anyInt())).thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/api/search/sale/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }

    @Nested
    @DisplayName("관련 상품 목록 조회 API 테스트")
    class GetRelatedSales {
        @Test
        @DisplayName("관련 상품 목록 조회 API 성공")
        void testGetRelatedSalesAPI() throws Exception {
            // given
            List<SaleRelatedDto> relatedSales = List.of(
                    SaleRelatedDto.builder()
                            .saleId(1)
                            .isLike(true)
                            .reviewCount(10L)
                            .build(),
                    SaleRelatedDto.builder()
                            .saleId(1)
                            .isLike(true)
                            .reviewCount(10L)
                            .build()
            );
            when(saleService.getRelatedSales(anyInt(), anyInt(), any(Pageable.class))).thenReturn(relatedSales);

            // when
            // then
            mvc.perform(get("/api/search/sale/related/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(GET_RELATED_SALES_SUCCESS.getMessage())))
                    .andExpect(jsonPath("$.data.size()", equalTo(2)));
        }

        @Test
        @DisplayName("관련 상품 목록 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetRelatedSalesAPI_SaleNotFoundException() throws Exception {
            // given
            when(saleService.getRelatedSales(anyInt(), anyInt(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/api/search/sale/related/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }

    @Nested
    @DisplayName("상품의 리뷰 목록 조회 API 테스트")
    class GetSaleReviewsTests {

        @Test
        @DisplayName("상품의 리뷰 목록 조회 API 성공")
        void testGetSaleReviewsAPI() throws Exception {
            // given
            List<ReviewDetailDto> reviews = List.of(
                new ReviewDetailDto(),
                new ReviewDetailDto()
            );
            Pageable pageable = PageRequest.of(0, 10);
            PageImpl<ReviewDetailDto> reviewDetails = new PageImpl<>(reviews, pageable, 2);
            PageResponseDto<ReviewDetailDto> result = new PageResponseDto<>(reviewDetails.getContent(), reviewDetails);
            when(saleService.getSaleReviews(anyInt(), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(get("/api/search/sale/review/{saleId}", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_REVIEWS_SUCCESS.getMessage())))
                    .andExpect(jsonPath("$.data.contents.size()", equalTo(2)));
        }

        @Test
        @DisplayName("상품의 리뷰 목록 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetSaleReviewsAPI_SaleNotFoundException() throws Exception {
            // given
            when(saleService.getSaleReviews(anyInt(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/api/search/sale/review/{saleId}", 1))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }

    @Nested
    @DisplayName("상품의 문의 목록 조회 API 테스트")
    class GetSaleQuestionsTests {

        @Test
        @DisplayName("상품의 문의 목록 조회 API 성공")
        void testGetSaleQuestionsAPI() throws Exception {
            // given
            List<QuestionResponseDto> questions = List.of(
                    QuestionResponseDto.builder()
                            .title("question1")
                            .writer("user1")
                            .build(),
                    QuestionResponseDto.builder()
                            .title("question2")
                            .writer("user2")
                            .build()
            );
            PageResponseDto<QuestionResponseDto> result = new PageResponseDto<>();
            result.setContents(questions);
            when(saleService.getSaleQuestions(anyInt(), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(get("/api/search/sale/question/{saleId}", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_QUESTION_SUCCESS.getMessage())));
        }

        @Test
        @DisplayName("상품의 문의 목록 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetSaleQuestionsAPI_ResourceNotFoundException() throws Exception {
            // given
            when(saleService.getSaleQuestions(anyInt(), any(Pageable.class)))
                    .thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/api/search/sale/question/{saleId}", 1))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }

    @Test
    @DisplayName("상위 50개의 상품 목록 조회 API 테스트")
    void testGetSaleRank() throws Exception {
        // given
        PageResponseDto<SaleRankingDto> sales = new PageResponseDto<>();
        sales.setPageSize(10);
        sales.setHasPrevious(false);
        sales.setHasNext(false);
        sales.setCurrentPage(0);
        sales.setPageSize(10);
        sales.setTotalElements(100L);
        when(saleService.getTop50Sales(anyInt(), any(Pageable.class))).thenReturn(sales);

        // when
        // then
        mvc.perform(get("/api/search/sale/rank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(GET_SALES_RANK.getMessage())))
                .andExpect(jsonPath("$.data.pageSize", equalTo(10)))
                .andExpect(jsonPath("$.data.hasPrevious", equalTo(false)))
                .andExpect(jsonPath("$.data.hasNext", equalTo(false)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalElements", equalTo(100)));
    }

    @Test
    @DisplayName("카테고리의 상품 목록 조회 API 테스트")
    void testGetSalesByType() throws Exception {
        // given
        PageResponseDto<SaleSummaryDto> sales = new PageResponseDto<>();
        sales.setPageSize(10);
        sales.setHasPrevious(false);
        sales.setHasNext(false);
        sales.setCurrentPage(0);
        sales.setPageSize(10);
        sales.setTotalElements(100L);
        when(saleService.getSalesByCategory(anyInt(), any(CategorySearchRequestDto.class), any(Pageable.class))).thenReturn(sales);

        // when
        // then
        mvc.perform(get("/api/search/sale/type")
                        .param("bigTypeId", "2")
                        .param("typeId", "6")
                        .param("sort", "RECOMMEND"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(GET_SALES_BY_CATEGORIES.getMessage())));
    }
}
