package com.farmdora.farmdora.user.depot.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DepotMassage {

    DEPOT_REGISTER_SUCCESS("배송지 등록 성공"),
    DEPOT_REGISTER_FAILURE("배송지 등록 실패"),
    DEPOT_MODIFY_SUCCESS("배송지 수정 성공"),
    DEPOT_MODIFY_FAILURE("배송지 수정 실패"),
    DEPOT_DELETE_SUCCESS("배송지 삭제 성공"),
    DEPOT_DELETE_FAILURE("배송지 삭제 실패"),
    DEPOT_GET_ALL_SUCCESS("해당 유저 전체 배송지 조회 성공"),
    DEPOT_GET_SUCCESS("해당 유저 특정 배송지 조회 성공"),
    USER_ADDRESS_GET_SUCCESS("유정 주소 조회 성공"),
    USER_ADDRESS_GET_FAILURE("유정 주소 조회 성공");

    private final String message;

}
