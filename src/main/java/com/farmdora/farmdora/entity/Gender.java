package com.farmdora.farmdora.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE(0), FEMALE(1);

    private final int value;
}
