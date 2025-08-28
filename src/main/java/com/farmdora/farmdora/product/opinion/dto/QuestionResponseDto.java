package com.farmdora.farmdora.product.opinion.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class QuestionResponseDto {
    private Integer questionId;
    private String userName;
    private String saleTitle;
    private String questionTitle;
    private LocalDateTime createdDate;
    private boolean isProcess;

    @QueryProjection
    public QuestionResponseDto(Integer questionId,
                               String userName,
                               String saleTitle,
                               String questionTitle,
                               LocalDateTime createdDate,
                               boolean isProcess) {
        this.questionId = questionId;
        this.userName = userName;
        this.saleTitle = saleTitle;
        this.questionTitle = questionTitle;
        this.createdDate = createdDate;
        this.isProcess = isProcess;
    }
}
