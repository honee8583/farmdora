package com.farmdora.farmdora.product.basket.controller;

import com.farmdora.farmdorabuyer.basket.dto.BasketRequestDto;
import com.farmdora.farmdorabuyer.basket.dto.BasketResponseDto;
import com.farmdora.farmdorabuyer.basket.service.BasketService;
import com.farmdora.farmdorabuyer.common.response.HttpResponse;
import com.farmdora.farmdorabuyer.common.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.*;

@RestController
@RequestMapping("${api.prefix}/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<?> addBasket(Principal principal, @RequestBody BasketRequestDto basketRequest) {
        Integer userId = Integer.parseInt(principal.getName());
        basketService.addBasket(userId, basketRequest);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, ADD_BASKET_SUCCESS.getMessage(), null));
    }

    @GetMapping
    public ResponseEntity<?> getBaskets(Principal principal, @PageableDefault(size = 5) Pageable pageable) {
        Integer userId = Integer.parseInt(principal.getName());
        PageResponseDTO<BasketResponseDto> baskets = basketService.getBaskets(userId, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_BASKETS_SUCCESS.getMessage(), baskets));
    }

    @DeleteMapping
    public ResponseEntity<?> removeBaskets(Principal principal, @RequestBody List<Integer> basketIds) {
        Integer userId = Integer.parseInt(principal.getName());
        basketService.removeBaskets(userId, basketIds);
        return ResponseEntity.ok().body(new HttpResponse(HttpStatus.OK, REMOVE_BASKET_SUCCESS.getMessage(), null));
    }

    @PutMapping("/{basketId}")
    public ResponseEntity<?> updateQuantity(Principal principal,
                                            @PathVariable("basketId") Integer basketId,
                                            @RequestParam int quantity) {
        Integer userId = Integer.parseInt(principal.getName());
        basketService.updateBasketQuantity(userId, basketId, quantity);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, UPDATE_BASKET_QUANTITY_SUCCESS.getMessage(), null));
    }
}
