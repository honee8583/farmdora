package com.farmdora.farmdora.order.orders.service;

import com.farmdora.farmdorabuyer.common.exception.AccessDeniedException;
import com.farmdora.farmdorabuyer.common.exception.FileException;
import com.farmdora.farmdorabuyer.common.exception.ResourceNotFoundException;
import com.farmdora.farmdorabuyer.common.response.PageResponseDTO;
import com.farmdora.farmdorabuyer.common.util.NcpImageProperties;
import com.farmdora.farmdorabuyer.entity.*;
import com.farmdora.farmdorabuyer.orders.dto.ReviewDTO.*;
import com.farmdora.farmdorabuyer.orders.dto.SearchDTO;
import com.farmdora.farmdorabuyer.orders.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;
    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;
    private final NCPObjectStorageService ncpImageService;
    private final OrderOptionRepository orderOptionRepository;
    private final SaleFileRepository saleFileRepository;
    private final UserRepository userRepository;
    private final NcpImageProperties ncpImageProperties;

    @Transactional
    public void createReview(Integer userId, ReviewRequest request, List<MultipartFile> files) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", userId));

        Sale sale = saleRepository.findById(request.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("sale", request.getSaleId()));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("order", request.getOrderId()));

        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException();
        }

        // 리뷰 생성 및 저장
        Review review = Review.builder()
                .order(order)
                .sale(sale)
                .score(request.getScore())
                .content(request.getContent())
                .build();
        reviewRepository.save(review);

        for(MultipartFile file: files) {
            try {
                String originalFilename = file.getOriginalFilename();
                String savedFilename  = ncpImageService.uploadImage(file, "review");

                // 리뷰 파일 정보 저장
                ReviewFile reviewFile = ReviewFile.builder()
                        .review(review)
                        .originFile(originalFilename)
                        .saveFile(savedFilename)
                        .build();

                reviewFileRepository.save(reviewFile);
            } catch (Exception e) {
                throw new FileException("파일을 저장할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReviewResponse> getMyReviews(Integer userId, SearchDTO searchDTO, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAllByOrderUserUserIdAndCreatedDateBetweenOrderByCreatedDateDesc(
                userId,
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                pageable);

        // 같은 orderId를 가진 리뷰들을 sale_id 기준으로 그룹화
        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream()
                .collect(Collectors.groupingBy(
                        review -> review.getOrder().getId(),
                        Collectors.groupingBy(review -> review.getSale().getId())
                ))
                .values().stream()
                .flatMap(orderGroup -> orderGroup.values().stream())
                .map(saleReviews -> {
                    Review review = saleReviews.get(0);
                    List<ReviewFile> reviewFiles = reviewFileRepository.findByReviewId(review.getId());
                    List<OrderOption> orderOptions = orderOptionRepository.findByOrderId(review.getOrder().getId());

                    List<OrderOptionInfo> filteredOrderOptions = orderOptions.stream()
                            .filter(option -> option.getOption().getSale().getId().equals(review.getSale().getId()))
                            .map(OrderOptionInfo::fromEntity)
                            .toList();

                    Optional<SaleFile> saleFile = saleFileRepository.findBySaleIdAndIsMainFalse(review.getSale().getId());
                    String productImageUrl = null;
                    if (saleFile.isPresent()) {
                        productImageUrl = ncpImageProperties.getProduct().createImageUrl(saleFile.get().getSaveFile());
                    }

                    List<String> reviewImageUrls = reviewFiles.stream()
                            .map(r -> ncpImageProperties.getReview().createImageUrl(r.getSaveFile()))
                            .toList();

                    return ReviewResponse.fromEntity(review, reviewImageUrls, filteredOrderOptions, productImageUrl);
                })
                .collect(Collectors.toList());

        return new PageResponseDTO<>(reviewPage, reviewResponses);
    }

    @Transactional
    public ReviewResponse updateReview(
            Integer reviewId,
            byte score,
            String content,
            List<String> removedImageUrls,
            MultipartFile[] newImages) throws IOException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("review", reviewId));
        review.updateReview(score, content);

        if(removedImageUrls != null && !removedImageUrls.isEmpty()) {
            for(String imageUrl : removedImageUrls) {
                String reviewFileName = extractFilenameDeleteUrl(imageUrl);
                ReviewFile reviewFile = reviewFileRepository.findBySaveFileAndReviewId(reviewFileName, reviewId);

                if(reviewFile != null) {
                    String savedFileName = reviewFile.getSaveFile();

                    String fullPath = savedFileName.startsWith("review/")
                            ? savedFileName
                            : "review/" + savedFileName;

                    ncpImageService.delete(fullPath);
                    reviewFileRepository.delete(reviewFile);
                }
            }
        }

        List<ReviewFile> newUpdatedReviewFiles = new ArrayList<>(reviewFileRepository.findAllByReviewId(reviewId));
        if (newImages != null) {
            for (MultipartFile image : newImages) {
                String UpdateFileName = ncpImageService.uploadImage(image, "review");

                ReviewFile reviewFile = ReviewFile.builder()
                        .review(review)
                        .originFile(image.getOriginalFilename())
                        .saveFile(UpdateFileName)
                        .build();

                reviewFileRepository.save(reviewFile);
                newUpdatedReviewFiles.add(reviewFile);
            }
        }

        List<OrderOption> orderOptions = orderOptionRepository.findByOrderId(review.getOrder().getId());

        // 리뷰의 saleId와 동일한 옵션만 필터링
        List<OrderOptionInfo> filteredOrderOptions = orderOptions.stream()
                .filter(option -> option.getOption().getSale().getId().equals(review.getSale().getId()))
                .map(OrderOptionInfo::fromEntity)
                .collect(Collectors.toList());

        // 해당 상품의 이미지 목록 조회 (업데이트 시에도 이미지 정보 추가)
        Optional<SaleFile> saleFile = saleFileRepository.findBySaleIdAndIsMainFalse(review.getSale().getId());
        String saleImageUrl = null;
        if (saleFile.isPresent()) {
            saleImageUrl = saleFile.get().getSaveFile();
        }

        // 응답 DTO 생성 및 반환
        List<String> updatedReviewFileUrls = newUpdatedReviewFiles.stream()
                .map(r -> ncpImageProperties.getReview().createImageUrl(r.getSaveFile()))
                .toList();
        return ReviewResponse.fromEntity(review, updatedReviewFileUrls, filteredOrderOptions, saleImageUrl);
    }

    private String extractFilenameDeleteUrl(String imageUrl) {
        // URL에서 쿼리 파라미터 제거
        if (imageUrl.contains("?")) {
            imageUrl = imageUrl.substring(0, imageUrl.indexOf("?"));
        }

        // URL에서 파일명 추출
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        if (imageUrl.contains("/review/")) {
            fileName = "review/" + fileName;
        }

        return fileName;
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("review", reviewId));

        List<ReviewFile> reviewFiles = reviewFileRepository.findAllByReviewId(reviewId);

        for(ReviewFile reviewFile : reviewFiles) {
            try {
                ncpImageService.delete(reviewFile.getSaveFile());
                reviewFileRepository.delete(reviewFile);
            } catch (Exception e) {
                log.error("리뷰삭제 실패 : {}", reviewFile.getSaveFile(), e);
            }
        }

        reviewRepository.delete(review);
    }
}