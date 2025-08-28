package com.farmdora.farmdora.search.opinion.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ProcessType;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private OpinionService opinionService;

    @Test
    @DisplayName("문의 목록 검색 서비스 로직 테스트")
    void testSearchQuestions() {
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

        when(questionRepository.searchQuestions(anyInt(), any(OpinionSearchRequestDto.class), any(Pageable.class))).thenReturn(questionPage);

        // when
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .searchType(SearchType.BUYER)
                .keyword("user")
                .searchPeriod(SearchPeriod.TODAY)
                .processTypes(List.of(ProcessType.WAIT))
                .sort(Sort.OLDEST)
                .build();
        PageResponseDto<QuestionResponseDto> result = opinionService.searchQuestions(1, searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("리뷰 목록 검색 서비스 로직 테스트")
    void testSearchReviews() {
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

        when(reviewRepository.searchReviews(anyInt(), any(OpinionSearchRequestDto.class), any(Pageable.class))).thenReturn(reviewPage);

        // when
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .searchType(SearchType.PRODUCT)
                .keyword("user")
                .searchPeriod(SearchPeriod.TODAY)
                .sort(Sort.OLDEST)
                .build();
        PageResponseDto<ReviewResponseDto> result = opinionService.searchReviews(1, searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }
}