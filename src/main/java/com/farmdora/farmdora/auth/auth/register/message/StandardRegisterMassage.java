package com.farmdora.farmdora.auth.auth.register.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StandardRegisterMassage {

    ID_CHECK_SUCCESS("아이디 중복체크에 성공하였습니다."),
    EMAIL_CHECK_SUCCESS("이메일 중복체크에 성공하였습니다."),
    EMAIL_SEND_SUCCESS("이메일 전송 성공하였습니다."),
    EMAIL_VERIFY_SUCCESS("이메일 인증 성공하였습니다."),
    EMAIL_VERIFY_FAIL("이메일 인증 실패하였습니다."),
    USER_REGISTER_SUCCESS("유저 등록 성공하였습니다."),
    SELLER_REGISTER_SUCCESS("판매자 신청 성공하였습니다.")
    ;

    private final String message;
}
