package com.farmdora.farmdora.product.opinion.repository;

import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomReviewRepository {
    Page<ReviewResponseDto> searchReviews(Integer userId, OpinionSearchRequestDto searchCondition, Pageable pageable);
}
