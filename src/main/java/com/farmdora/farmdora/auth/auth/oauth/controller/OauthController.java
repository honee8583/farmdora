package com.farmdora.farmdora.auth.auth.oauth.controller;

import com.farmdora.farmdoraauth.auth.StringKey.StringKey;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Environment env;

    @PostMapping("/id/save")
    public HttpResponse idSave(@RequestBody Map<String, String> map, HttpServletRequest request) {

        String provider = map.get(StringKey.provider);

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.replace("Bearer ", "").trim();
        }else {
            throw new RuntimeException("연동 실패");
        }

        log.info("idSave {}", provider);

        redisTemplate.opsForValue().set(StringKey.frontFromToken, token, Duration.ofMinutes(5));

        String redirectUrl = env.getProperty("social.redirect.url") + provider;

        log.info("redirectUrl = {}", redirectUrl);
        return HttpResponse.builder()
                .message("아이디 저장성공")
                .data(redirectUrl)
                .build();
    }
}
