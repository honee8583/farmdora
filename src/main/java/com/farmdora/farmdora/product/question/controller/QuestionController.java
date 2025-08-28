package com.farmdora.farmdora.product.question.controller;

import com.farmdora.farmdorabuyer.common.response.HttpResponse;
import com.farmdora.farmdorabuyer.question.dto.QuestionRequestDTO;
import com.farmdora.farmdorabuyer.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/{saleId}")
    public ResponseEntity<?> addQuestion(Principal principal,
                                         @PathVariable("saleId") Integer saleId,
                                         @RequestBody QuestionRequestDTO request) {
        Integer userId = Integer.parseInt(principal.getName());
        questionService.addQuestion(userId, saleId, request);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, "문의 등록에 성공하였습니다.", null));
    }
}
