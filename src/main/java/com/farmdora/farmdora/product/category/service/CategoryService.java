package com.farmdora.farmdora.product.category.service;

import com.farmdora.farmdora.category.dto.CategoryResponseDto;
import com.farmdora.farmdora.category.dto.CategoryResponseDto.CategoryDto;
import com.farmdora.farmdora.category.repository.SaleTypeRepository;
import com.farmdora.farmdora.entity.SaleType;
import com.farmdora.farmdora.entity.SaleTypeBig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final SaleTypeRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories() {
        List<SaleType> saleTypes = categoryRepository.findAll();
        Map<Short, CategoryResponseDto> categoryMap = createCategoryMap(saleTypes);
        return new ArrayList<>(categoryMap.values());
    }

    private Map<Short, CategoryResponseDto> createCategoryMap(List<SaleType> saleTypes) {
        Map<Short, CategoryResponseDto> categoryMap = new LinkedHashMap<>();
        for (SaleType saleType : saleTypes) {
            SaleTypeBig bigType = saleType.getSaleTypeBig();
            CategoryResponseDto categoryResponseDto = categoryMap.computeIfAbsent(bigType.getId(), id ->
                    CategoryResponseDto.builder()
                            .bigCategoryId(id)
                            .name(bigType.getName())
                            .categories(new ArrayList<>())
                            .build()
            );

            categoryResponseDto.getCategories().add(
                    new CategoryDto(saleType.getId(), saleType.getName())
            );
        }
        return categoryMap;
    }
}
