package com.farmdora.farmdora.auth.exception;

import com.farmdora.farmdora.common.error.exception.CustomException;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException() {
        super(ErrorMessage.INVALID_PASSWORD.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
