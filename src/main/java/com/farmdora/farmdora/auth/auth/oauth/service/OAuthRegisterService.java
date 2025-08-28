package com.farmdora.farmdora.auth.auth.oauth.service;

import com.farmdora.farmdoraauth.auth.oauth.repository.SnsRegisterRepository;
import com.farmdora.farmdoraauth.auth.oauth.repository.SnsTypeRepository;
import com.farmdora.farmdoraauth.entity.Sns;
import com.farmdora.farmdoraauth.entity.SnsType;
import com.farmdora.farmdoraauth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthRegisterService {

    private final SnsRegisterRepository snsRegisterRepository;
    private final SnsTypeRepository snsTypeRepository;
    private final Environment env;

    public void registerOAuth(int userId, String provider, String snsName) {

        short typeId = snsTypeRepository.findByName(provider).getId();

        if (snsRegisterRepository.existsBySnsName(snsName)) {
            log.info("이미 등록된 SNS 계정: {}", snsName);
            return; // 중복이면 추가로 insert하지 않음
        }

        Sns sns = Sns.builder()
                .user(User.builder().userId(userId).build())
                .type(SnsType.builder().id(typeId).build())
                .snsName(snsName)
                .build();

        snsRegisterRepository.save(sns);
    }
}
