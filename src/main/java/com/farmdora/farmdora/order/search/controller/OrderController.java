package com.farmdora.farmdora.order.search.controller;

import com.farmdora.farmdora.auth.PrincipalUtil;
import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.search.dto.OrderSearchResponseDto;
import com.farmdora.farmdora.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_ORDER_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/my/seller/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final PrincipalUtil principalUtil;

    @GetMapping
    public ResponseEntity<?> searchOrders(Principal principal,
                                          OrderSearchRequestDto searchCondition,
                                          @PageableDefault Pageable pageable) {
        Integer userId = principalUtil.extractUserIdRequired(principal);
        PageResponseDto<OrderSearchResponseDto> orders = orderService.searchOrders(userId, searchCondition, pageable);
        return ResponseEntity
                .ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_ORDER_SUCCESS.getMessage(), orders));
    }
}
