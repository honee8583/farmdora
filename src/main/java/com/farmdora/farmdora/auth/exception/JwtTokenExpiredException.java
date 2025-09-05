package com.farmdora.farmdora.auth.exception;

import com.farmdora.farmdora.common.error.exception.CustomException;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class JwtTokenExpiredException extends CustomException {

    public JwtTokenExpiredException() {
        super(ErrorMessage.TOKEN_EXPIRED.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
