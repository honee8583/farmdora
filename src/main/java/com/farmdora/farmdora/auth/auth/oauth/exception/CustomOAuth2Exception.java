package com.farmdora.farmdora.auth.auth.oauth.exception;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class CustomOAuth2Exception extends OAuth2AuthenticationException {
    private final String snsName;
    private final String provider;
    private final String token;

    public CustomOAuth2Exception(String msg, String snsName, String provider, String token) {
        super(msg);
        this.snsName = snsName;
        this.provider = provider;
        this.token = token;
    }

}

