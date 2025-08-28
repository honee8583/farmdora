package com.farmdora.farmdora.auth.auth.oauth.factory;

import com.farmdora.farmdoraauth.auth.oauth.oauthDto.GoogleUserInfo;
import com.farmdora.farmdoraauth.auth.oauth.oauthDto.KakaoUserInfo;
import com.farmdora.farmdoraauth.auth.oauth.oauthDto.NaverUserInfo;
import com.farmdora.farmdoraauth.auth.oauth.oauthDto.OAuthUserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuthUserInfoFactory {
    public static OAuthUserInfo getOAuthUserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider.toLowerCase()){
            case "kakao" -> new KakaoUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            case "google" -> new GoogleUserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        };
    }
}
