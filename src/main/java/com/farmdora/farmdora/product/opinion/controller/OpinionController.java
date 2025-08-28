package com.farmdora.farmdora.product.opinion.controller;

import com.farmdora.farmdora.auth.PrincipalUtil;
import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.opinion.service.OpinionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_QUESTION_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_REVIEWS_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/my/seller/order")
@RequiredArgsConstructor
public class OpinionController {
    private final OpinionService opinionService;
    private final PrincipalUtil principalUtil;

    @GetMapping("/inquiry")
    public ResponseEntity<?> searchQuestions(Principal principal,
                                             OpinionSearchRequestDto searchCondition,
                                             @PageableDefault Pageable pageable) {
        Integer userId = principalUtil.extractUserIdNotRequired(principal);
        PageResponseDto<QuestionResponseDto> questions = opinionService.searchQuestions(userId, searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_QUESTION_SUCCESS.getMessage(), questions));
    }

    @GetMapping("/review")
    public ResponseEntity<?> searchReviews(Principal principal,
                                           OpinionSearchRequestDto searchCondition,
                                           @PageableDefault Pageable pageable) {
        Integer userId = principalUtil.extractUserIdNotRequired(principal);
        PageResponseDto<ReviewResponseDto> reviews = opinionService.searchReviews(userId, searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_REVIEWS_SUCCESS.getMessage(), reviews));
    }
}
