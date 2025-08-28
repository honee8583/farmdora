package com.farmdora.farmdora.auth.auth.oauth.handler;

import com.farmdora.farmdoraauth.auth.oauth.exception.CustomOAuth2Exception;
import com.farmdora.farmdoraauth.auth.oauth.service.OAuthRegisterService;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private final OAuthRegisterService oAuthRegisterService;
    private final JwtUtil jwtUtil;
    private final Environment env;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            response.sendRedirect(env.getProperty("front.redirect.url")+"/login?error=blind");
        }else if (exception instanceof CredentialsExpiredException) {
            response.sendRedirect(env.getProperty("front.redirect.url")+"/login?error=expired");
        }else if (exception instanceof CustomOAuth2Exception) {
            response.sendRedirect(env.getProperty("front.redirect.url")+"/login?error=fail");
        }
    }
}
