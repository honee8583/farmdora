package com.farmdora.farmdora.order.orders.controller;

import com.farmdora.farmdorabuyer.common.response.HttpResponse;
import com.farmdora.farmdorabuyer.common.response.PageResponseDTO;
import com.farmdora.farmdorabuyer.orders.dto.OrderResponseDTO;
import com.farmdora.farmdorabuyer.orders.dto.SearchDTO;
import com.farmdora.farmdorabuyer.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.CANCEL_ORDER_SUCCESS;
import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.SEARCH_ORDER_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/my/user")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<?> getOrderList(Principal principal,
                                          @ModelAttribute SearchDTO SearchDTO,
                                          @PageableDefault(size = 5) Pageable pageable) {
        Integer userId = Integer.parseInt(principal.getName());
        PageResponseDTO<OrderResponseDTO> result = orderService.getOrderList(userId, SearchDTO, pageable);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_ORDER_SUCCESS.getMessage(), result));
    }

    @PutMapping("/order/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Integer orderId) {
        boolean isCancelOrder = orderService.cancelOrder(orderId);

        if(isCancelOrder) {
            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, CANCEL_ORDER_SUCCESS.getMessage(), null));
        } else {
            return ResponseEntity.badRequest()
                    .body(new HttpResponse(HttpStatus.BAD_REQUEST, "주문 취소에 실패했습니다", null));
        }
    }
}