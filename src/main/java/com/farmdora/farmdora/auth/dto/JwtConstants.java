package com.farmdora.farmdora.auth.dto;

public final class JwtConstants {
    private JwtConstants() {};

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String CLAIM_SUBJECT = "token";
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_ROLE = "role";
}
