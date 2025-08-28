package com.farmdora.farmdora.user.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public LocalDateTime getStartDate() {
        return startDate != null ? startDate.atStartOfDay() : null;
    }

    public LocalDateTime getEndDate() {
        return endDate != null ? endDate.atTime(23, 59, 59) : null;
    }
}