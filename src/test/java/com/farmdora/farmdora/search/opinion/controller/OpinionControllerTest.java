package com.farmdora.farmdora.search.opinion.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_QUESTION_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_REVIEWS_SUCCESS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OpinionControllerTest extends ControllerTest {

    @Test
    @DisplayName("문의 목록 검색 API 테스트")
    void testSearchQuestions() throws Exception {
        // given
        List<QuestionResponseDto> questions = List.of(
                QuestionResponseDto.builder()
                        .questionId(1)
                        .userName("user1")
                        .saleTitle("sale1")
                        .questionTitle("question1")
                        .createdDate(LocalDateTime.now())
                        .isProcess(true)
                        .build(),
                QuestionResponseDto.builder()
                        .questionId(2)
                        .userName("user2")
                        .saleTitle("sale2")
                        .questionTitle("question2")
                        .createdDate(LocalDateTime.now())
                        .isProcess(true)
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<QuestionResponseDto> questionPage = new PageImpl<>(questions, pageable, 2);
        PageResponseDto<QuestionResponseDto> result = new PageResponseDto<>(questions, questionPage);
        when(opinionService.searchQuestions(anyInt(), any(OpinionSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

        // when
        // then
        mvc.perform(get("/api/search/my/seller/order/inquiry")
                        .param("sellerId", "1")
                        .param("searchType", "PRODUCT")
                        .param("keyword", "상추")
                        .param("searchPeriod", "TODAY")
                        .param("processTypes", "WAIT")
                        .param("sort", "LATEST")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(SEARCH_QUESTION_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.contents.size()", equalTo(2)));
    }

    @Test
    @DisplayName("리뷰 목록 검색 API 테스트")
    void testSearchReviews() throws Exception {
        // given
        List<ReviewResponseDto> reviews = List.of(
                ReviewResponseDto.builder()
                        .saleTitle("sale")
                        .reviewContent("review1")
                        .writer("user1")
                        .createdDate(LocalDateTime.now())
                        .score((byte) 4)
                        .build(),
                ReviewResponseDto.builder()
                        .saleTitle("sale")
                        .reviewContent("review2")
                        .writer("user2")
                        .createdDate(LocalDateTime.now())
                        .score((byte) 3)
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(reviews, pageable, 2);
        PageResponseDto<ReviewResponseDto> result = new PageResponseDto<>(reviews, reviewPage);
        when(opinionService.searchReviews(anyInt(), any(OpinionSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

        // when
        // then
        mvc.perform(get("/api/search/my/seller/order/review")
                        .param("sellerId", "1")
                        .param("searchType", "PRODUCT")
                        .param("keyword", "상추")
                        .param("searchPeriod", "TODAY")
                        .param("sort", "LATEST")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(SEARCH_REVIEWS_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.contents.size()", equalTo(2)));
    }
}