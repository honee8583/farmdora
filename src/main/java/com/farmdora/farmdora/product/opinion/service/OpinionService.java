package com.farmdora.farmdora.product.opinion.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OpinionService {
    private final QuestionRepository questionRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<QuestionResponseDto> searchQuestions(Integer userId, OpinionSearchRequestDto searchCondition, Pageable pageable) {
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(userId, searchCondition, pageable);
        return new PageResponseDto<>(questions.getContent(), questions);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ReviewResponseDto> searchReviews(Integer userId, OpinionSearchRequestDto searchCondition, Pageable pageable) {
        Page<ReviewResponseDto> reviews = reviewRepository.searchReviews(userId, searchCondition, pageable);
        return new PageResponseDto<>(reviews.getContent(), reviews);
    }
}
