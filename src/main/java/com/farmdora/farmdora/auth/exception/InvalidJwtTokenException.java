package com.farmdora.farmdora.auth.exception;

import com.farmdora.farmdora.common.error.exception.CustomException;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends CustomException {

    public InvalidJwtTokenException() {
        super(ErrorMessage.INVALID_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
