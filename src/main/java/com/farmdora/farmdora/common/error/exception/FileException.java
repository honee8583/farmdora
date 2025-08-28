package com.farmdora.farmdora.common.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileException extends CustomException {

    public FileException(String message, HttpStatus status) {
        super(message, status);
    }
}
