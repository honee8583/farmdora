package com.farmdora.farmdora.common.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends CustomException {
    public AuthenticationException() {
        super("로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED);
    }
}

