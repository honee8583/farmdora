package com.farmdora.farmdora.auth.auth.find.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FindMessage {

    EMAIL_SUB("이메일 인증"),
    EMAIL_TITLE ("이메일 인증 요청"),
    EMAIL_CONTENT ("안녕하세요! 아래의 인증 코드를 일력하여 인증을 완료하세요:"),
    INFO_GET_SUCCESS("유저 정보 조회 성공");

    private final String message;
}
