package com.farmdora.farmdora.search.admin.service;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.dto.PopupSearchResponseDto;
import com.farmdora.farmdora.admin.dto.PopupTypeDto;
import com.farmdora.farmdora.admin.repository.PopupRepository;
import com.farmdora.farmdora.admin.repository.PopupTypeRepository;
import com.farmdora.farmdora.common.NcpImageProperties;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Popup;
import com.farmdora.farmdora.entity.PopupType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PopupServiceTest {

    @Mock
    private PopupRepository popupRepository;

    @Mock
    private PopupTypeRepository popupTypeRepository;

    @Mock
    private NcpImageProperties ncpImageProperties;

    @InjectMocks
    private PopupService popupService;

    @Test
    @DisplayName("팝업 검색 서비스 레이어 테스트")
    void testSearchPopups() {
        // given
        List<Popup> popups = List.of(
                Popup.builder()
                        .title("popup1")
                        .type(PopupType.builder().id((short) 1).build())
                        .saveFile("image")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build(),
                Popup.builder()
                        .title("popup2")
                        .type(PopupType.builder().id((short) 1).build())
                        .saveFile("image")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Popup> popupPages = new PageImpl<>(popups, pageable, 2);
        when(popupRepository.searchPopups(any(PopupSearchRequestDto.class), any(Pageable.class))).thenReturn(popupPages);

        NcpImageProperties.ImageInfo bannerMock = Mockito.mock(NcpImageProperties.ImageInfo.class);
        when(ncpImageProperties.getBanner()).thenReturn(bannerMock);
        when(bannerMock.createImageUrl(anyString())).thenReturn("https://mocked-url/image.jpg");

        // when
        PopupSearchRequestDto searchCondition = PopupSearchRequestDto.builder()
                .keyword("popup")
                .build();
        PageResponseDto<PopupSearchResponseDto> result = popupService.searchPopups(searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("팝업 타입 목록 조회 서비스 레이어 테스트")
    void testGetPopupTypes() {
        // given
        List<PopupType> popupTypes = List.of(
                PopupType.builder()
                        .id(Short.valueOf("1"))
                        .name("이벤트1")
                        .build(),
                PopupType.builder()
                        .id(Short.valueOf("2"))
                        .name("이벤트2")
                        .build()
        );
        when(popupTypeRepository.findAll()).thenReturn(popupTypes);

        // when
        List<PopupTypeDto> result = popupService.getPopupTypes();

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}