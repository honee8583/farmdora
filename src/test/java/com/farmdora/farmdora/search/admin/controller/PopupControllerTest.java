package com.farmdora.farmdora.search.admin.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.dto.PopupSearchResponseDto;
import com.farmdora.farmdora.admin.dto.PopupTypeDto;
import com.farmdora.farmdora.common.response.PageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_POPUPS_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_POPUP_TYPES_SUCCESS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PopupControllerTest extends ControllerTest {

    @Test
    @DisplayName("팝업 목록 검색 API 테스트")
    void testSearchPopups() throws Exception {
        // given
        List<PopupSearchResponseDto> popups = List.of(
                PopupSearchResponseDto.builder()
                        .title("popup1")
                        .imageUrl("image")
                        .build(),
                PopupSearchResponseDto.builder()
                        .title("popup2")
                        .imageUrl("image")
                        .build()
        );
        PageResponseDto<PopupSearchResponseDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContents(popups);
        pageResponseDto.setTotalElements(2L);

        when(popupService.searchPopups(any(PopupSearchRequestDto.class), any(Pageable.class))).thenReturn(pageResponseDto);

        // when
        // then
        mvc.perform(get("/api/search/admin/popup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(GET_POPUPS_SUCCESS.getMessage())));
    }

    @Test
    @DisplayName("팝업 타입 목록 조회 API 테스트")
    void testGetPopupTypes() throws Exception {
        // given
        List<PopupTypeDto> popupTypes = List.of(
                PopupTypeDto.builder()
                        .id(1)
                        .name("이벤트")
                        .build(),
                PopupTypeDto.builder()
                        .id(2)
                        .name("배너")
                        .build()
        );
        when(popupService.getPopupTypes()).thenReturn(popupTypes);

        // when
        // then
        mvc.perform(get("/api/search/admin/popup/type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo(GET_POPUP_TYPES_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.size()", equalTo(2)));
    }
}