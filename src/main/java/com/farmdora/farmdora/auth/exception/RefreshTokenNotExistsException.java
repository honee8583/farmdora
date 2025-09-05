package com.farmdora.farmdora.auth.exception;

import com.farmdora.farmdora.common.error.exception.CustomException;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotExistsException extends CustomException {

    public RefreshTokenNotExistsException() {
        super(ErrorMessage.EMPTY_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
