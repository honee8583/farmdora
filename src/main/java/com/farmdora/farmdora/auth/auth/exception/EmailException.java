package com.farmdora.farmdora.auth.auth.exception;

import com.farmdora.farmdoraauth.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailException extends BaseException {
    public EmailException(String message, HttpStatus status) {
        super(message,status);
    }
}
