package com.farmdora.farmdora.product.opinion.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class ReviewResponseDto {
    private Integer reviewId;
    private String saleTitle;
    private String reviewContent;
    private String writer;
    private String reply;
    private LocalDateTime createdDate;
    private byte score;

    @QueryProjection
    public ReviewResponseDto(Integer reviewId,
                             String saleTitle,
                             String reviewContent,
                             String writer,
                             String reply,
                             LocalDateTime createdDate,
                             byte score) {
        this.reviewId = reviewId;
        this.saleTitle = saleTitle;
        this.reviewContent = reviewContent;
        this.writer = writer;
        this.reply = reply;
        this.createdDate = createdDate;
        this.score = score;
    }
}
