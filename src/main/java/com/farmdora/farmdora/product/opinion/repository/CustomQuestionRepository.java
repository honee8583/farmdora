package com.farmdora.farmdora.product.opinion.repository;

import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuestionRepository {
    Page<QuestionResponseDto> searchQuestions(Integer sellerId, OpinionSearchRequestDto searchCondition, Pageable pageable);
}
