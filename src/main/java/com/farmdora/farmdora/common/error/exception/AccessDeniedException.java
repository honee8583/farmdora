package com.farmdora.farmdora.common.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccessDeniedException extends CustomException {
    public AccessDeniedException() {
        super("접근 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
