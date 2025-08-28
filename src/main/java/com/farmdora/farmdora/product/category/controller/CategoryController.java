package com.farmdora.farmdora.product.category.controller;

import com.farmdora.farmdora.category.dto.CategoryResponseDto;
import com.farmdora.farmdora.category.service.CategoryService;
import com.farmdora.farmdora.common.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_CATEGORIES_SUCCESS;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategories() {
        List<CategoryResponseDto> categories = categoryService.getCategories();
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_CATEGORIES_SUCCESS.getMessage(), categories));
    }
}
