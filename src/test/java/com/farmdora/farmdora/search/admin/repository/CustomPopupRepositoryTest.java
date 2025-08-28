package com.farmdora.farmdora.search.admin.repository;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.entity.Popup;
import com.farmdora.farmdora.entity.PopupType;
import com.farmdora.farmdora.order.dto.Sort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomPopupRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PopupRepository popupRepository;

    @Test
    @DisplayName("배너/이벤트 사진 목록 조회 QueryDsl 테스트")
    void testSearchPopups() {
        // given
        PopupType typePopup = PopupType.builder()
                .id((short) 1)
                .name("배너")
                .build();
        em.persist(typePopup);
        PopupType typeEvent = PopupType.builder()
                .id((short) 2)
                .name("이벤트")
                .build();
        em.persist(typeEvent);

        for (int i = 1; i <= 20; i++) {
            Popup popup = null;
            if (i % 2 == 0) {
                popup = Popup.builder()
                        .title("banner" + i)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now())
                        .type(typePopup)
                        .build();
            } else {
                popup = Popup.builder()
                        .title("event" + i)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now())
                        .type(typeEvent)
                        .build();
            }
            em.persist(popup);
        }

        // when
        PopupSearchRequestDto bannerSearchCondition = PopupSearchRequestDto.builder()
                .keyword("ban")
                .types(List.of(typePopup.getId()))
                .sort(Sort.LATEST)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Popup> banners = popupRepository.searchPopups(bannerSearchCondition, pageable);

        PopupSearchRequestDto eventSearchCondition = PopupSearchRequestDto.builder()
                .keyword("eve")
                .types(List.of(typeEvent.getId(), typePopup.getId()))
                .sort(Sort.OLDEST)
                .build();
        Page<Popup> events = popupRepository.searchPopups(eventSearchCondition, pageable);

        // then
        assertThat(banners.getContent().size()).isEqualTo(10);
        assertThat(banners.getTotalElements()).isEqualTo(10);

        assertThat(events.getContent().size()).isEqualTo(10);
        assertThat(events.getTotalElements()).isEqualTo(10);
    }
}