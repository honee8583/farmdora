package com.farmdora.farmdora.user.event.service;

import com.farmdora.farmdoraactivity.admin.dto.PopupDTO.*;
import com.farmdora.farmdoraactivity.admin.dto.SearchDTO;
import com.farmdora.farmdoraactivity.admin.dto.SortType;
import com.farmdora.farmdoraactivity.admin.repository.PopupRepository;
import com.farmdora.farmdoraactivity.admin.repository.PopupTypeRepository;
import com.farmdora.farmdoraactivity.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraactivity.common.response.PageResponseDto;
import com.farmdora.farmdoraactivity.entity.Popup;
import com.farmdora.farmdoraactivity.entity.PopupType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private final PopupTypeRepository popupTypeRepository;
    private final NCPObjectStorageService ncpObjectStorageService;

    // 팝업/배너 이미지 저장 경로
    private static final String POPUP_FOLDER = "popup";

    /**
     * 팝업 타입 목록 조회
     */
    @Transactional(readOnly = true)
    public List<PopupTypeInfo> getPopTypes() {
        return popupTypeRepository.findAll().stream()
                .map(PopupTypeInfo::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 팝업/배너 등록
     */
    @Transactional
    public void createPopup(PopupRequest request, MultipartFile file) throws IOException {
        // 팝업 타입 조회
        PopupType popupType = popupTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("popupType", request.getTypeId()));

        // 이미지 파일 저장
        String originalFilename = "";
        String savedFilename = "";

        if (file != null && !file.isEmpty()) {
            originalFilename = file.getOriginalFilename();
            savedFilename = ncpObjectStorageService.uploadImage(file, POPUP_FOLDER);
        }

        // 팝업 엔티티 생성 및 저장
        Popup popup = Popup.builder()
                .type(popupType)
                .title(request.getTitle())
                .originFile(originalFilename)
                .saveFile(savedFilename)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        popupRepository.save(popup);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<PopupListResponse> getPopups(SortType sortType, SearchDTO searchDTO, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Popup> popupPage;

        // 1. 타입만 선택한 경우 (EVENT 또는 BANNER)
        if (sortType != null && (sortType.equals(SortType.EVENT) || sortType.equals(SortType.BANNER))) {
            Short typeId = sortType.equals(SortType.EVENT) ? (short) 1 : (short) 2;

            // 1-1. 타입과 날짜를 같이 선택한 경우
            if (searchDTO != null && (searchDTO.getStartDate() != null || searchDTO.getEndDate() != null)) {
                popupPage = popupRepository.findByTypeIdAndDateBetween(
                        typeId,
                        searchDTO.getStartDate(),
                        searchDTO.getEndDate(),
                        pageable);
            }
            // 1-2. 타입만 선택한 경우
            else {
                popupPage = popupRepository.findByTypeIdOrderByEndDateAsc(typeId, pageable);
            }
        }
        // 2. 날짜만 선택한 경우
        else if (searchDTO != null && (searchDTO.getStartDate() != null || searchDTO.getEndDate() != null)) {
            popupPage = popupRepository.findAllByConditions(
                    searchDTO.getStartDate(),
                    searchDTO.getEndDate(),
                    pageable);
        }
        // 3. 아무것도 선택하지 않은 경우 (전체 조회)
        else {
            popupPage = popupRepository.findAllOrderByEndDate(pageable);
        }

        List<PopupListResponse> popupResponses = popupPage.getContent().stream()
                .map(popup -> PopupListResponse.fromEntity(popup, ncpObjectStorageService))
                .collect(Collectors.toList());

        return new PageResponseDto<>(popupResponses, popupPage);
    }

    @Transactional(readOnly = true)
    public PopupResponse getPopup(Integer id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("popup", id));

        return PopupResponse.fromEntity(popup, ncpObjectStorageService);
    }

    @Transactional
    public PopupResponse updatePopup(Integer id, PopupRequest request, MultipartFile file) throws IOException {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("popup", id));

        // 팝업 타입 조회
        PopupType popupType = popupTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("popupType", request.getTypeId()));

        // 기본 정보 직접 수정
        popup.setType(popupType);
        popup.setTitle(request.getTitle());
        popup.setStartDate(request.getStartDate());
        popup.setEndDate(request.getEndDate());

        popupRepository.save(popup);

        // 이미지 파일 업데이트
        if (file != null && !file.isEmpty()) {
            // 기존 이미지가 있다면 삭제 처리
            if (popup.getSaveFile() != null && !popup.getSaveFile().isEmpty()) {
                String fullPath = popup.getSaveFile().startsWith("popup/")
                        ? popup.getSaveFile()
                        : "popup/" + popup.getSaveFile();

                ncpObjectStorageService.delete(fullPath);
            }

            // 새 이미지 업로드
            String originalFilename = file.getOriginalFilename();
            String savedFilename = ncpObjectStorageService.uploadImage(file, POPUP_FOLDER);

            // 파일 정보 직접 수정
            popup.setOriginFile(originalFilename);
            popup.setSaveFile(savedFilename);

            popupRepository.save(popup);
        }

        // 응답 DTO 생성 및 반환
        return PopupResponse.fromEntity(popup, ncpObjectStorageService);
    }

    /**
     * 팝업/배너 삭제
     */
    @Transactional
    public void deletePopup(Integer id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("popup", id));

        // 이미지 파일 삭제
        if (popup.getSaveFile() != null && !popup.getSaveFile().isEmpty()) {
            String fullPath = popup.getSaveFile().startsWith("popup/")
                    ? popup.getSaveFile()
                    : "popup/" + popup.getSaveFile();

            ncpObjectStorageService.delete(fullPath);
        }

        // DB에서 팝업 삭제
        popupRepository.delete(popup);
    }
}