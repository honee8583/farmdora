package com.farmdora.farmdora.common.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends CustomException {
    private final Object data;

    public ResourceNotFoundException(String resourceName, Object data) {
        super(String.format("%s 데이터가 존재하지 않습니다 : '%s'", resourceName, data), HttpStatus.BAD_REQUEST);
        this.data = data;
    }
}
