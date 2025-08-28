package com.farmdora.farmdora.auth.auth.oauth.handler;

import com.farmdora.farmdoraauth.auth.StringKey.StringKey;
import com.farmdora.farmdoraauth.auth.oauth.service.OAuthLoginService;
import com.farmdora.farmdoraauth.auth.oauth.service.OAuthRegisterService;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final OAuthLoginService oAuthLoginService;
    private final Environment env;
    private final OAuthRegisterService oAuthRegisterService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            log.info("1");
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

                String snsName = (String) oAuth2User.getAttributes().get("snsName");
                String provider = (String) oAuth2User.getAttributes().get("provider");

                log.info("snsName: {}, provider: {}", snsName, provider);

            Object frontFromTokenObj = redisTemplate.opsForValue().get(StringKey.frontFromToken);

            redisTemplate.delete(StringKey.frontFromToken);
            log.info("frontFromId: {}", frontFromTokenObj);

            String frontToken = "";

            if (frontFromTokenObj != null) {
                frontToken=String.valueOf(frontFromTokenObj);
                int userId = jwtUtil.getUserId(frontToken);
                try {
                    log.info("userId: {}", userId);

                    oAuthRegisterService.registerOAuth(userId, provider, snsName);

                    log.info("Registered OAuth token: {}", userId);
                     authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, jwtUtil.getAuthorities(frontToken));
                    log.info("소셜 연동 토큰 {}", frontToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    response.sendRedirect(env.getProperty("front.redirect.url")+"/?success=oauthregister");
                    return;
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendRedirect(env.getProperty("front.redirect.url")+"/my/user/profile?error=oauthregister");
//                    return;
                }
                return;
            }


            User user =oAuthLoginService.oauthLogin(snsName);

            int userId = user.getUserId();
            String role = user.getAuth().getRole();

            String token = jwtUtil.createJwt(userId, role, user.getId(),60 * 60 * 10L * 1000);

            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("블랙리스트에 등록된 토큰으로 로그인 시도 불가");
                return;
            }

            try {
                redisTemplate.opsForValue().set(StringKey.accessToken + userId, token, Duration.ofHours(5));
            } catch (Exception e) {
                e.printStackTrace();
            }

            authentication = new UsernamePasswordAuthenticationToken(userId, null, jwtUtil.getAuthorities(token));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setContentType("application/json;charset=utf-8");
            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setHttpOnly(false);
//            cookie.setSecure(false);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setAttribute("SameSite", "None");
            cookie.setMaxAge(60 * 60 * 5);
            response.addCookie(cookie);
            response.sendRedirect(env.getProperty("front.redirect.url"));
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(env.getProperty("front.redirect.url")+"/login?error=oauthlogin");
        }
    }
}
