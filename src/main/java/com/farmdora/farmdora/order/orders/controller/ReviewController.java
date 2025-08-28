package com.farmdora.farmdora.order.orders.controller;

import com.farmdora.farmdorabuyer.common.response.HttpResponse;
import com.farmdora.farmdorabuyer.common.response.PageResponseDTO;
import com.farmdora.farmdorabuyer.orders.dto.ReviewDTO.*;
import com.farmdora.farmdorabuyer.orders.dto.SearchDTO;
import com.farmdora.farmdorabuyer.orders.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.REGISTER_REVIEW_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/my/user")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    
    @PostMapping("/order/review")
    public ResponseEntity<?> createReview(
            Principal principal,
            ReviewRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        Integer userId = Integer.parseInt(principal.getName());
        List<MultipartFile> imagesList = images != null ? Arrays.asList(images) : new ArrayList<>();
        reviewService.createReview(userId, request, imagesList);
        return ResponseEntity
                .ok()
                .body(new HttpResponse(HttpStatus.OK, REGISTER_REVIEW_SUCCESS.getMessage(), null));
    }
  
    @GetMapping("/order/myreviews")
    public ResponseEntity<?> getMyReviews(Principal principal,
                                          @ModelAttribute SearchDTO searchDTO,
                                          @PageableDefault(size = 5) Pageable pageable) {
        Integer userId = Integer.parseInt(principal.getName());
        PageResponseDTO<ReviewResponse> pageResponse = reviewService.getMyReviews(userId, searchDTO, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, "리뷰를 성공적으로 조회했습니다.", pageResponse));
    }
    
    @PutMapping(value = "/order/myreviews/{reviewId}")
    public ResponseEntity<?> updateMyReviews(
            @PathVariable("reviewId") Integer reviewId,
            @RequestParam("score") byte score,
            @RequestParam("content") String content,
            @RequestParam(value = "removedImageUrls", required = false) String[] removedImageUrls,
            @RequestParam(value = "images", required = false) MultipartFile[] newImages) throws IOException {
        List<String> removedImageList = removedImageUrls != null ? Arrays.asList(removedImageUrls) : new ArrayList<>();
        ReviewResponse updatedReview = reviewService.updateReview(
                reviewId,
                score,
                content,
                removedImageList,
                newImages
        );
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, "리뷰를 성공적으로 수정했습니다.", updatedReview));
    }
    
    @DeleteMapping("/order/myreviews/{reviewId}/delete")
    public ResponseEntity<?> deleteMyReview(@PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, "리뷰를 성공적으로 삭제했습니다.", null));
    }
}