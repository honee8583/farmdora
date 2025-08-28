package com.farmdora.farmdora.auth.auth.oauth.service;

import com.farmdora.farmdoraauth.auth.oauth.factory.OAuthUserInfoFactory;
import com.farmdora.farmdoraauth.auth.oauth.oauthDto.OAuthUserInfo;
import com.farmdora.farmdoraauth.auth.oauth.repository.SnsRepository;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final SnsRepository snsRepository;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuthUserInfo oAuthUserInfo = OAuthUserInfoFactory.getOAuthUserInfo(provider, attributes);
        String snsName = oAuthUserInfo.getProvider() + "_" + oAuthUserInfo.getProviderId();

        attributes.put("snsName", snsName);
        attributes.put("provider", provider);

        User user = snsRepository.findUserBySnsName(snsName).isPresent() ? snsRepository.findUserBySnsName(snsName).get() : null;

        if (user != null && user.isExpire()) {
            throw new DisabledException("차단된 계정입니다.");
        }

        if (user != null && user.isBlind()) {
            throw new CredentialsExpiredException("탈퇴한 계정입니다.");
        }

        if (user != null) {
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(user.getAuth().getRole())),
                    attributes,
                    "snsName");
        } else {
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST")),
                    attributes,
                    "snsName"
            );
        }
    }
}