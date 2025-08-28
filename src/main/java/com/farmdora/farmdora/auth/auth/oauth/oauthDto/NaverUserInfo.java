package com.farmdora.farmdora.auth.auth.oauth.oauthDto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NaverUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        Map<String, Object> attribute = (Map<String, Object>) attributes.get("response");
        return attribute.get("id").toString();
    }

}
