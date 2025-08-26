package com.farmdora.farmdora.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    ENTITY_NOT_FOUND("데이터가 존재하지 않습니다. ");

    private final String message;
}
