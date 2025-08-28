package com.farmdora.farmdora.auth.auth.login.controller;

import com.farmdora.farmdoraauth.auth.login.service.LoginService;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/login")
public class LoginRestController {

    private final LoginService loginService;


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "jwt_token", required = false) String cookieToken,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    Principal principal) {
        Integer userId = Integer.parseInt(principal.getName());
        String headerToken = extractToken(request);
        String token = (headerToken != null) ? headerToken : cookieToken;

        HttpResponse result = loginService.logout(token, userId);

        // 클라이언트의 jwt_token 쿠키 만료시키기
        ResponseCookie expiredCookie = ResponseCookie.from("jwt_token", "")
                .httpOnly(false)
                .secure(true) // 운영 환경에서는 true
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());

        return ResponseEntity.ok(new HttpResponse());
    }
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "").trim();
        }
        return null;
    }
}
