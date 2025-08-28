package com.farmdora.farmdora.user.event.controller;

import com.farmdora.farmdoraactivity.admin.dto.PopupDTO;
import com.farmdora.farmdoraactivity.admin.dto.SearchDTO;
import com.farmdora.farmdoraactivity.admin.dto.SortType;
import com.farmdora.farmdoraactivity.admin.service.PopupService;
import com.farmdora.farmdoraactivity.common.response.HttpResponse;
import com.farmdora.farmdoraactivity.common.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/user/popup")
@RequiredArgsConstructor
public class UserEventController {

    private final PopupService popupService;

    @GetMapping
    public ResponseEntity<?> getPopups(
            @RequestParam(required = false) SortType sortType,
            SearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            PageResponseDto<PopupDTO.PopupListResponse> popups = popupService.getPopups(sortType, searchDTO, page);
            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, "이벤트 목록 조회에 성공했습니다.", popups));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPopup(@PathVariable Integer id) {
        try {
            PopupDTO.PopupResponse popup = popupService.getPopup(id);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, "이벤트 상세조회에 성공했습니다.", popup));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 상세조회 중 오류가 발생했습니다: " + e.getMessage(), null));
        }
    }

}
