package com.farmdora.farmdora.product.category.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Short bigCategoryId;
    private String name;
    private List<CategoryDto> categories;

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto {
        private Short categoryId;
        private String name;
    }
}
