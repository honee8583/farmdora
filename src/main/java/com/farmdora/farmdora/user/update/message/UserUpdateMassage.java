package com.farmdora.farmdora.user.update.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserUpdateMassage {

    PASSWORD_VERIFY_SUCCESS("비밀번호 검증 성공하셨습니다."),
    PASSWORD_VERIFY_FAILURE("비밀번호 검증 실패하셨습니다."),
    USER_SELECT_SUCCESS("해당 유저 조회 성공하셨습니다."),
    USER_MODIFY_SUCCESS("회원 정보 수정 성공하셨습니다."),
    USER_MODIFY_FAILURE("회원 정보 수정 실패하셨습니다."),
    USER_EXPIRE_SUCCESS("회원 탈퇴 성공하셨습니다.")
    ;

    private final String message;
}
