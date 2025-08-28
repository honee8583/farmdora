package com.farmdora.farmdora.product.popup.controller;

import com.farmdora.farmdorabuyer.common.response.HttpResponse;
import com.farmdora.farmdorabuyer.popup.dto.PopupDTO;
import com.farmdora.farmdorabuyer.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.GET_POPUPS_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/popup")
@RequiredArgsConstructor
public class PopupController {
    private final PopupService popupService;

    @GetMapping
    public ResponseEntity<?> getPopups() {
        List<PopupDTO> popups = popupService.getPopups();
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_POPUPS_SUCCESS.getMessage(), popups));
    }
}
