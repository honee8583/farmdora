package com.farmdora.farmdora.user.event.dto;

import com.farmdora.farmdoraactivity.admin.service.NCPObjectStorageService;
import com.farmdora.farmdoraactivity.entity.Popup;
import com.farmdora.farmdoraactivity.entity.PopupType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class PopupDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupTypeInfo {
        private Short typeId;
        private String name;

        public static PopupTypeInfo fromEntity(PopupType popupType) {
            return PopupTypeInfo.builder()
                    .typeId(popupType.getId())
                    .name(popupType.getName())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupRequest {
        private Short typeId;
        private String title;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startDate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endDate;
        // 파일 정보는 MultipartFile로 컨트롤러에서 직접 받음
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupResponse {
        private Integer id;
        private PopupTypeInfo type;
        private String title;
        private String imageUrl;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;

        // Popup 엔티티를 PopupResponse DTO로 변환하는 정적 메서드
        public static PopupResponse fromEntity(Popup popup, NCPObjectStorageService ncpObjectStorageService) {
            // 이미지 URL 생성
            String imageUrl = null;
            if (popup.getSaveFile() != null && !popup.getSaveFile().isEmpty()) {
                imageUrl = ncpObjectStorageService.getObjectStorageImageUrl(popup.getSaveFile());
            }

            return PopupResponse.builder()
                    .id(popup.getId())
                    .type(PopupTypeInfo.fromEntity(popup.getType()))
                    .title(popup.getTitle())
                    .imageUrl(imageUrl)
                    .startDate(popup.getStartDate())
                    .endDate(popup.getEndDate())
                    .createdAt(popup.getCreatedDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupListResponse {
        private Integer id;
        private PopupTypeInfo type;
        private String title;
        private String imageUrl;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public static PopupListResponse fromEntity(Popup popup, NCPObjectStorageService ncpObjectStorageService) {
            String imageUrl = null;
            if (popup.getSaveFile() != null && !popup.getSaveFile().isEmpty()) {
                if (popup.getType() != null) {
                    Short typeId = popup.getType().getId();
                    if (typeId == 1) { // 이벤트 타입
                        imageUrl = ncpObjectStorageService.getEventImageUrl(popup.getSaveFile());
                    } else if (typeId == 2) { // 배너 타입
                        imageUrl = ncpObjectStorageService.getBannerImageUrl(popup.getSaveFile());
                    } else {
                        imageUrl = ncpObjectStorageService.getObjectStorageImageUrl(popup.getSaveFile());
                    }
                } else {
                    imageUrl = ncpObjectStorageService.getObjectStorageImageUrl(popup.getSaveFile());
                }
            }

            return PopupListResponse.builder()
                    .id(popup.getId())
                    .type(PopupTypeInfo.fromEntity(popup.getType()))
                    .title(popup.getTitle())
                    .imageUrl(imageUrl)
                    .startDate(popup.getStartDate())
                    .endDate(popup.getEndDate())
                    .build();
        }
    }
}