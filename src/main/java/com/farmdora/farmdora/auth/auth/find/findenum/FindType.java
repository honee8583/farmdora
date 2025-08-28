package com.farmdora.farmdora.auth.auth.find.findenum;

import lombok.Getter;

@Getter
public enum FindType {
    ID("아이디 찾기","아이디 찾기 요청"
            ,"안녕하세요! 아래의 아이디로 로그인 해주세요"),
    PWD("임시 비밀번호","비밀번호 찾기 요청"
            ,"안녕하세요! 임시 비밀번호가 발급 되었습니다. 아래의 비밀번호로 로그인 한 후 비밀번호를 변경해주세요"),;

    private final String subject;
    private final String title;
    private final String content;

    FindType(String subject, String title, String content) {
        this.subject = subject;
        this.title = title;
        this.content = content;
    }

}
