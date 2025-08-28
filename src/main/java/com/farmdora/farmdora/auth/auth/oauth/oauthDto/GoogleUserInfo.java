package com.farmdora.farmdora.auth.auth.oauth.oauthDto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }
}
