package com.farmdora.farmdora.user.depot.service;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.Depot;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.*;
import com.farmdora.farmdoraauth.mypage.user.depot.mapper.DepotMapper;
import com.farmdora.farmdoraauth.mypage.user.depot.repository.DepotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DepotService {
    private final DepotRepository depotRepository;
    private final UserRepository userRepository;
    private final DepotMapper depotMapper;

    public List<DepotSelectResponseDto> getDepotsByUserId(int userId) {

        List<Depot> depotList = depotRepository.findByUser_UserId(userId);

        if (depotList.isEmpty()) {
            throw new ResourceNotFoundException("배송지 정보가 없습니다.", userId);
        }

        return depotList.stream()
                .map(depotMapper::toDto)
                .toList();
    }

    public DepotSelectResponseDto getDepotById(int depotId) {
        Depot depot = depotRepository.findById(depotId)
                .orElseThrow(()->new ResourceNotFoundException("배송지 상세 보기",depotId));

        return depotMapper.toDto(depot);
    }

    public void registerDepot(DepotRegisterRequestDto depotRegisterRequestDto) {
        // User 엔티티를 조회하여 데이터 무결성 확보
        User user = userRepository.findById(depotRegisterRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("배송지 추가 또는 수정 무결성", depotRegisterRequestDto.getUserId()));

        // Depot 엔티티 생성
        Depot depot = depotMapper.toEntity(depotRegisterRequestDto,user);

        // isDefault 처리
        if (depot.isDefault()) {
            depotRepository.updateIsDefaultToFalse(user.getUserId());
        }
        log.info("배송지 저장 {}", depot);
        // Depot 엔티티 저장
        depotRepository.save(depot);
    }

    public void modifyDepot(DepotModifyRequestDto depotModifyRequestDto) {
        log.info("기본 배송지{}",depotModifyRequestDto.isDefaultAddr());

        // User 엔티티를 조회하여 데이터 무결성 확보
        Depot existingDepot = depotRepository.findById(depotModifyRequestDto.getDepotId())
                .orElseThrow(() -> new ResourceNotFoundException("배송지 정보 없음", depotModifyRequestDto.getDepotId()));

        // isDefault 처리
        if (depotModifyRequestDto.isDefaultAddr()) {
            depotRepository.updateIsDefaultToFalse(existingDepot.getUser().getUserId());
        }

        depotMapper.updateEntityFormDto(depotModifyRequestDto, existingDepot);

        // Depot 엔티티 저장
        depotRepository.save(existingDepot);
    }

    public void deleteDepot(int depotId) {
        depotRepository.findById(depotId)
                .orElseThrow(() -> new ResourceNotFoundException("삭제 대상 배송지", depotId));

        depotRepository.deleteById(depotId);
    }

    public UserAddressDto getUserAddr(Integer userId) {
        return userRepository.findUserAddressByUserId(userId);
    }
}
