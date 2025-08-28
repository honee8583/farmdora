package com.farmdora.farmdora.auth.auth.login.service;

import com.farmdora.farmdoraauth.auth.StringKey.StringKey;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public HttpResponse logout(String token,Integer userId) {
        try {
            if (token == null) {
                return HttpResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("유효한 토큰이 없습니다.")
                        .build();
            }

            log.info("로그아웃 요청 - userId: {}, token: {}", userId, token);

            // Redis 블랙리스트 등록
            long expiration = jwtUtil.getExpiration(token);
            redisTemplate.opsForValue().set(StringKey.blackList + token, "logout", Duration.ofMillis(expiration));
            log.info("블랙리스트 등록 완료");

            // Redis 캐시 토큰 삭제
            if (userId != -1) {
                redisTemplate.delete(StringKey.accessToken + userId);
                log.info("Redis accessToken 삭제 완료 - userId: {}", userId);
            }

            SecurityContextHolder.clearContext();
            log.info("SecurityContextHolder 초기화 완료");

            return HttpResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("로그아웃 성공")
                    .build();

        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생", e);
            return HttpResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("서버 오류로 로그아웃에 실패했습니다.")
                    .build();
        }
    }
}
