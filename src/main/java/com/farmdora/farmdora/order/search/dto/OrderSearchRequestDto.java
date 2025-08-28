package com.farmdora.farmdora.order.search.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequestDto {
    private SearchType searchType;
    private String keyword;
    private SearchPeriod searchPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder.Default
    private List<Short> statusIds = new ArrayList<>();

    @Builder.Default
    private Sort sort = Sort.LATEST;
}
